package com.example.warehouse.service;

import com.example.warehouse.config.WarehouseNotificationServiceConfig;
import com.example.warehouse.dto.WarehouseDto;
import com.example.warehouse.exception.WarehouseFormatException;
import com.example.warehouse.model.WarehouseLog;
import com.example.warehouse.model.WarehouseView;
import com.example.warehouse.repository.WarehouseRepository;
import com.example.warehouse.utils.WarehouseDtoConverter;
import com.example.warehouse.utils.WarehouseViewLogComposer;
import com.example.warehouse.utils.WarehouseViewLogUpdater;
import com.example.warehouse.validator.WarehouseValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.TransactionCanceledException;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseValidator warehouseValidator;
    private final WarehouseDtoConverter warehouseDtoConverter;
    private final WarehouseViewLogComposer warehouseViewLogComposer;
    private final WarehouseViewLogUpdater warehouseViewLogUpdater;
    private final SnsClient snsClient;
    private final ObjectWriter objectWriter;
    private final WarehouseNotificationServiceConfig notificationServiceConfig;

    @Retryable(retryFor = TransactionCanceledException.class)
    public void save(WarehouseDto warehouseDto) {
        var warehouseLogs = warehouseDtoConverter.convertToLogs(warehouseDto);
        var warehouseIds = getIdsFromLogs(warehouseLogs);
        var warehouseViews = warehouseRepository.getViewsByIds(warehouseIds);
        var warehouseViewLogCompositions = warehouseViewLogComposer.composeAll(warehouseLogs, warehouseViews);
        warehouseViewLogUpdater.updateAllViewsFromLogs(warehouseViewLogCompositions);
        warehouseValidator.validateWarehouseViews(warehouseViews);
        warehouseRepository.updateWarehouseWithViews(warehouseViews, warehouseLogs);
    }

    public void sendWarehouseDto(WarehouseDto warehouseDto) {
        var messageGroupId = warehouseDto.getOrderId();
        var deduplicationId = warehouseDto.getOrderId();
        var jsonPayment = getJsonPayment(warehouseDto);
        var publishRequestDynamoEndpoint = PublishRequest.builder()
                .message(jsonPayment)
                .messageGroupId(messageGroupId)
                .messageDeduplicationId(deduplicationId)
                .topicArn(notificationServiceConfig.topicArn())
                .build();

        snsClient.publish(publishRequestDynamoEndpoint);
    }


    public List<WarehouseView> warehouseViews(){
        return warehouseRepository.getAll();
    }

    private static List<String> getIdsFromLogs(Set<WarehouseLog> warehouseLogs) {
        return warehouseLogs
                .stream()
                .map(WarehouseLog::getProductId)
                .toList();
    }

    private String getJsonPayment(WarehouseDto warehouseDto) {
        try {
            return objectWriter.writeValueAsString(warehouseDto);
        } catch (JsonProcessingException e) {
            log.warn("fail to process json with payments: {}", e.getMessage());
            throw new WarehouseFormatException(e.getMessage());
        }
    }
}
