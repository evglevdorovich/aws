package com.example.warehouse.listener;

import com.example.warehouse.config.QueueConfigProperties;
import com.example.warehouse.dto.WarehouseDto;
import com.example.warehouse.service.WarehouseEventDispatcher;
import com.example.warehouse.service.WarehouseService;
import com.example.warehouse.status.WarehouseEventStatus;
import com.example.warehouse.utils.ObjectMessageExtractor;
import com.example.warehouse.utils.WarehouseReasonChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseListener {

    private final QueueConfigProperties queueConfigProperties;
    private final SqsClient sqsClient;
    private final ObjectMessageExtractor<WarehouseDto> objectMessageExtractor;
    private final WarehouseService warehouseService;
    private final WarehouseEventDispatcher warehouseEventDispatcher;
    private final WarehouseReasonChecker warehouseReasonChecker;

    @Scheduled(cron = "*/5 * * * * *")
    public void onWarehouseMessagesReceived() {
        String queueUrl = getQueueUrl();
        List<Message> messages = receiveMessages(queueUrl);

        for (var message : messages) {
            var warehouseDto = objectMessageExtractor.extractFromMessage(message, WarehouseDto.class);
            try {
                warehouseService.save(warehouseDto);
                if (warehouseReasonChecker.isWithdrawal(warehouseDto)) {
                    warehouseEventDispatcher.dispatch(warehouseDto.getOrderId(), WarehouseEventStatus.SUCCESS);
                    deleteMessage(queueUrl, message);
                }
            } catch (Exception exception) {
                if (warehouseReasonChecker.isWithdrawal(warehouseDto)) {
                    warehouseEventDispatcher.dispatch(warehouseDto.getOrderId(), WarehouseEventStatus.FAIL);
                    // not in finally - because if the exception will be thrown after dispatching it will delete the message from the queue
                    deleteMessage(queueUrl, message);
                }
            }
        }
    }

    private void deleteMessage(String queueUrl, Message message) {
        var deletePaymentRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(message.receiptHandle())
                .build();

        sqsClient.deleteMessage(deletePaymentRequest);
    }

    private List<Message> receiveMessages(String queueUrl) {
        var receiveMessageRequest = ReceiveMessageRequest
                .builder()
                .queueUrl(queueUrl)
                .waitTimeSeconds(queueConfigProperties.waitTimeSeconds())
                .maxNumberOfMessages(queueConfigProperties.maxNumberOfMessages())
                .build();

        return sqsClient.receiveMessage(receiveMessageRequest).messages();
    }

    private String getQueueUrl() {
        var getQueueUrlRequest = GetQueueUrlRequest.builder()
                .queueName(queueConfigProperties.name())
                .build();

        return sqsClient.getQueueUrl(getQueueUrlRequest).queueUrl();
    }

}
