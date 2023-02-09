package com.example.order.service;


import com.example.order.config.PaymentNotificationServiceConfig;
import com.example.order.dto.PaymentDto;
import com.example.order.exception.PaymentFormatException;
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

    public void sendPayment(PaymentDto paymentDto) {
        var messageGroupId = paymentDto.getUserId();
        var deduplicationId = paymentDto.getOrderId();
        var jsonPayment = getJsonPayment(paymentDto);
        var publishRequestDynamoEndpoint = PublishRequest.builder()
                .message(jsonPayment)
                .messageGroupId(messageGroupId)
                .messageDeduplicationId(deduplicationId)
                .topicArn(notificationServiceProperties.topicArn())
                .build();

        snsClient.publish(publishRequestDynamoEndpoint);
    }

    private String getJsonPayment(PaymentDto paymentDto) {
        try {
            return objectWriter.writeValueAsString(paymentDto);
        } catch (JsonProcessingException e) {
            log.warn("fail to process json with payments: {}", e.getMessage());
            throw new PaymentFormatException(e.getMessage());
        }
    }

}
