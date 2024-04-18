package com.example.payment.service;

import com.example.payment.config.PaymentNotificationServiceConfig;
import com.example.payment.exception.PaymentFormatException;
import com.example.payment.model.PaymentAccountLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final SnsClient snsClient;
    private final ObjectWriter objectWriter;
    private final PaymentNotificationServiceConfig notificationServiceProperties;

    public void sendPayment(PaymentAccountLog paymentAccountLog) {
        var messageGroupId = paymentAccountLog.getUserId();
        var deduplicationId = paymentAccountLog.getOrderId();
        var jsonPayment = getJsonPayment(paymentAccountLog);
        var publishRequestDynamoEndpoint = PublishRequest.builder()
                .message(jsonPayment)
                .messageGroupId(messageGroupId)
                .messageDeduplicationId(deduplicationId)
                .topicArn(notificationServiceProperties.topicArn())
                .build();

        snsClient.publish(publishRequestDynamoEndpoint);
    }

    private String getJsonPayment(PaymentAccountLog paymentAccountLog) {
        try {
            return objectWriter.writeValueAsString(paymentAccountLog);
        } catch (JsonProcessingException e) {
            log.warn("fail to process json with payments: {}", e.getMessage());
            throw new PaymentFormatException(e.getMessage());
        }
    }
}
