package com.example.order.listener;

import com.example.order.config.OrderProcessingQueueConfigProperties;
import com.example.order.model.OrderResult;
import com.example.order.strategy.factory.OrderResultStrategyFactory;
import com.example.order.utils.ObjectMessageExtractor;
import com.example.order.utils.OrderReasonExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderProcessingListener {
    private final SqsClient amazonSQS;
    private final OrderProcessingQueueConfigProperties queueConfigProperties;
    private final ObjectMessageExtractor<OrderResult> objectMessageExtractor;
    private final OrderReasonExtractor orderReasonExtractor;
    private final OrderResultStrategyFactory orderResultStrategyFactory;
    @Value("${sourceAttributeName}")
    private String sourceAttributeName;

    @Scheduled(cron = "*/5 * * * * *")
    public void onReceiveOrderStatus() {
        String queueUrl = getQueueUrl();
        List<Message> messages = receiveMessages(queueUrl);

        for (var message : messages) {
            var orderResult = (OrderResult) objectMessageExtractor.
                    extractFromMessageWithAttribute(message, sourceAttributeName, OrderResult.class);
            var orderResultWithExctractedReason = orderReasonExtractor.extractReason(orderResult);
            var strategy = orderResultStrategyFactory.getStrategy(orderResultWithExctractedReason);
            strategy.processResult(orderResult);

            deleteMessage(queueUrl, message);
        }
    }

    private void deleteMessage(String queueUrl, Message message) {
        var deletePaymentRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(message.receiptHandle())
                .build();

        amazonSQS.deleteMessage(deletePaymentRequest);
    }

    private List<Message> receiveMessages(String queueUrl) {
        var receiveMessageRequest = ReceiveMessageRequest
                .builder()
                .queueUrl(queueUrl)
                .waitTimeSeconds(queueConfigProperties.waitTimeSeconds())
                .maxNumberOfMessages(queueConfigProperties.maxNumberOfMessages())
                .build();

        return amazonSQS.receiveMessage(receiveMessageRequest).messages();
    }

    // could be separate class
    private String getQueueUrl() {
        var getQueueUrlRequest = GetQueueUrlRequest.builder()
                .queueName(queueConfigProperties.name())
                .build();

        return amazonSQS.getQueueUrl(getQueueUrlRequest).queueUrl();
    }
}
