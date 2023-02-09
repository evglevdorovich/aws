package com.example.warehouse.service;

import com.example.warehouse.config.WarehouseEventResultNotificationConfig;
import com.example.warehouse.exception.WarehouseResultFormatException;
import com.example.warehouse.model.WarehouseEventResult;
import com.example.warehouse.status.WarehouseEventStatus;
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

@Slf4j
@RequiredArgsConstructor
@Component
public class WarehouseEventDispatcher {
    private final SnsClient snsClient;
    private final ObjectWriter objectWriter;
    private final WarehouseEventResultNotificationConfig eventResultNotificationConfig;
    @Value("${event.source}")
    private String eventSource;

    public void dispatch(String orderId, WarehouseEventStatus warehouseEventStatus) {

        var paymentEventResult = WarehouseEventResult.builder()
                .orderId(orderId)
                .status(warehouseEventStatus)
                .build();

        var jsonPaymentResult = getJsonPayment(paymentEventResult);
        var eventSourceAttribute = getEventSourceAttribute();
        var warehousePublishRequest = PublishRequest.builder()
                .message(jsonPaymentResult)
                .messageGroupId(orderId)
                .messageDeduplicationId(orderId)
                .messageAttributes(eventSourceAttribute)
                .topicArn(eventResultNotificationConfig.topicArn())
                .build();

        snsClient.publish(warehousePublishRequest);
    }

    private Map<String, MessageAttributeValue> getEventSourceAttribute() {
        var eventSourceAttributeValue = MessageAttributeValue.builder()
                .stringValue(eventSource)
                .dataType("String")
                .build();
        return Map.of("source", eventSourceAttributeValue);
    }

    private String getJsonPayment(WarehouseEventResult eventResult) {
        try {
            return objectWriter.writeValueAsString(eventResult);
        } catch (JsonProcessingException e) {
            log.warn("fail to process json with warehouseResult: {}", e.getMessage());
            throw new WarehouseResultFormatException(e.getMessage());
        }
    }
}
