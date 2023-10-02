package com.example.warehouse.utils;

import com.example.warehouse.model.WarehouseLog;
import com.example.warehouse.model.WarehouseView;
import com.example.warehouse.model.WarehouseViewLogComposition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class WarehouseViewLogComposer {
    public List<WarehouseViewLogComposition> composeAll(Set<WarehouseLog> warehouseLogs, Set<WarehouseView> warehouseViews) {
        var logsByProductId = getLogsByProductId(warehouseLogs);
        var viewByProductId = getViewsByProductId(warehouseViews);

        return logsByProductId
                .entrySet()
                .stream()
                .map(log -> new WarehouseViewLogComposition(log.getValue(), viewByProductId.get(log.getKey())))
                .toList();
    }

    private static Map<String, WarehouseLog> getLogsByProductId(Set<WarehouseLog> warehouseLogs) {
        return warehouseLogs.stream()
                .collect(Collectors.toMap(WarehouseLog::getProductId, Function.identity()));
    }

    private static Map<String, WarehouseView> getViewsByProductId(Set<WarehouseView> warehouseViews) {
        return warehouseViews.stream()
                .collect(Collectors.toMap(WarehouseView::getProductId, Function.identity()));
    }

}
