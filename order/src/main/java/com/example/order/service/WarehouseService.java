package com.example.order.service;

import com.example.order.config.WarehouseNotificationServiceConfig;
import com.example.order.dto.WarehouseDto;
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
public class WarehouseService {
    //withdrawal
    private final SnsClient snsClient;
    private final ObjectWriter objectWriter;
    private final WarehouseNotificationServiceConfig notificationServiceConfig;
    public void sendProducts(WarehouseDto warehouseDto) {
        var warehouseDtoJson = getWarehouseDtoJson(warehouseDto);
        var messageGroupId = warehouseDto.getOrderId();
        var deduplicationId = warehouseDto.getOrderId();
        var warehousePublishRequest = PublishRequest.builder()
                .message(warehouseDtoJson)
                .messageGroupId(messageGroupId)
                .messageDeduplicationId(deduplicationId)
                .topicArn(notificationServiceConfig.topicArn())
                .build();

        snsClient.publish(warehousePublishRequest);
    }

    private String getWarehouseDtoJson(WarehouseDto warehouseDto) {
        try {
            return objectWriter.writeValueAsString(warehouseDto);
        } catch (JsonProcessingException e) {
            log.warn("fail to process json with ordered products: {}", e.getMessage());
            throw new PaymentFormatException(e.getMessage());
        }
    }
}
