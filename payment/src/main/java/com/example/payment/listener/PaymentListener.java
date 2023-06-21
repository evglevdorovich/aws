package com.example.payment.listener;

import com.example.payment.config.QueueConfigProperties;
import com.example.payment.exception.PaymentFailedException;
import com.example.payment.model.PaymentAccountLog;
import com.example.payment.repository.PaymentAccountRepository;
import com.example.payment.service.PaymentEventDispatcher;
import com.example.payment.status.PaymentStatus;
import com.example.payment.utils.ObjectMessageExtractor;
import com.example.payment.utils.PaymentLogReasonChecker;
import com.example.payment.validator.PaymentValidator;
import lombok.RequiredArgsConstructor;
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
public class PaymentListener {

    private final QueueConfigProperties queueConfigProperties;
    private final SqsClient sqsClient;
    private final PaymentAccountRepository paymentRepository;
    private final ObjectMessageExtractor<PaymentAccountLog> objectMessageExtractor;
    private final PaymentValidator paymentValidator;
    private final PaymentEventDispatcher paymentEventDispatcher;
    private final PaymentLogReasonChecker reasonChecker;

    @Scheduled(cron = "*/5 * * * * *")
    public void onPaymentsReceived() {
        String queueUrl = getQueueUrl();
        List<Message> messages = receiveMessages(queueUrl);

        for (var message : messages) {
            var paymentAccountLog = objectMessageExtractor.extractFromMessage(message, PaymentAccountLog.class);
            //TODO: migrate to separate service class
            try {
                var paymentAccountView = paymentRepository.getViewById(paymentAccountLog.getUserId());
                paymentValidator.validatePayment(paymentAccountLog, paymentAccountView);
                paymentAccountView.addAmount(paymentAccountLog.getAmountToDiff());
                paymentRepository.updatePaymentAccountWithView(paymentAccountLog, paymentAccountView);
                if (reasonChecker.isReceipt(paymentAccountLog)) {
                    paymentEventDispatcher.dispatch(paymentAccountLog, PaymentStatus.SUCCESS);
                }
                deleteMessage(queueUrl, message);
            } catch (PaymentFailedException paymentFailedException) {
                if (reasonChecker.isReceipt(paymentAccountLog)) {
                    paymentEventDispatcher.dispatch(paymentAccountLog, PaymentStatus.FAIL);
                }
                deleteMessage(queueUrl, message);
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
