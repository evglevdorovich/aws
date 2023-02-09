package com.example.payment.service;

import com.example.payment.config.PaymentEventResultNotificationConfig;
import com.example.payment.exception.PaymentResultFormatException;
import com.example.payment.model.PaymentAccountLog;
import com.example.payment.model.PaymentEventResult;
import com.example.payment.status.PaymentStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventDispatcher {
    private final SnsClient snsClient;
    private final ObjectWriter objectWriter;
    private final PaymentEventResultNotificationConfig paymentEventResultNotificationConfig;
    @Value("${event.source}")
    private String eventSource;

    public void dispatch(PaymentAccountLog paymentAccountLog, PaymentStatus paymentStatus) {
        var messageGroupId = paymentAccountLog.getOrderId();
        var deduplicationId = paymentAccountLog.getOrderId();
        var paymentEventResult = PaymentEventResult.builder()
                .orderId(paymentAccountLog.getOrderId())
                .status(paymentStatus)
                .build();

        var eventSourceAttribute = getEventSourceAttribute();

        var jsonPaymentResult = getJsonPayment(paymentEventResult);

        var paymentPublishRequest = PublishRequest.builder()
                .message(jsonPaymentResult)
                .messageGroupId(messageGroupId)
                .messageDeduplicationId(deduplicationId)
                .messageAttributes(eventSourceAttribute)
                .topicArn(paymentEventResultNotificationConfig.topicArn())
                .build();

        snsClient.publish(paymentPublishRequest);
    }

    private Map<String, MessageAttributeValue> getEventSourceAttribute() {
        var eventSourceAttributeValue = MessageAttributeValue.builder()
                .stringValue(eventSource)
                .dataType("String")
                .build();
        return Map.of("source", eventSourceAttributeValue);
    }

    private String getJsonPayment(PaymentEventResult paymentEventResult) {
        try {
            return objectWriter.writeValueAsString(paymentEventResult);
        } catch (JsonProcessingException e) {
            log.warn("fail to process json with paymentResult: {}", e.getMessage());
            throw new PaymentResultFormatException(e.getMessage());
        }
    }
}
