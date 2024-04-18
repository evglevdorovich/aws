package com.example.order.service;


import com.example.order.config.PaymentNotificationServiceConfig;
import com.example.order.dto.PaymentDepositDto;
import com.example.order.dto.PaymentDto;
import com.example.order.exception.PaymentFormatException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final SnsClient snsClient;
    private final ObjectWriter objectWriter;
    private final RestTemplate restTemplate;
    private final PaymentNotificationServiceConfig notificationServiceProperties;
    private final static String PAYMENT_CREATING = "http://payment:8081/paymentAccount/%s";

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

    public void addMoneyAtStart(String userId) {
        var paymentDepositDto = new PaymentDepositDto(UUID.randomUUID() + "_deposit",
                BigDecimal.valueOf(120_000_000L));
        restTemplate.patchForObject(String.format(PAYMENT_CREATING, userId),
                paymentDepositDto, PaymentDepositDto.class);
    }
}
