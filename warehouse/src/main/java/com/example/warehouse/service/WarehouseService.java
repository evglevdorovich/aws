package com.example.warehouse.service;

import com.example.warehouse.dto.WarehouseDto;
import com.example.warehouse.model.WarehouseLog;
import com.example.warehouse.repository.WarehouseRepository;
import com.example.warehouse.utils.WarehouseDtoConverter;
import com.example.warehouse.utils.WarehouseViewLogComposer;
import com.example.warehouse.utils.WarehouseViewLogUpdater;
import com.example.warehouse.validator.WarehouseValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.TransactionCanceledException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseValidator warehouseValidator;
    private final WarehouseDtoConverter warehouseDtoConverter;
    private final WarehouseViewLogComposer warehouseViewLogComposer;
    private final WarehouseViewLogUpdater warehouseViewLogUpdater;

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

    private static List<String> getIdsFromLogs(Set<WarehouseLog> warehouseLogs) {
        return warehouseLogs
                .stream()
                .map(WarehouseLog::getProductId)
                .toList();
    }
}
