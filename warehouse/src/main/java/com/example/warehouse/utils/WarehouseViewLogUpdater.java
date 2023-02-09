package com.example.warehouse.utils;

import com.example.warehouse.model.WarehouseViewLogComposition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WarehouseViewLogUpdater {
    public List<WarehouseViewLogComposition> updateAllViewsFromLogs(List<WarehouseViewLogComposition> warehouseViewLogCompositions) {

        warehouseViewLogCompositions.forEach(composition -> {
                    var log = composition.getWarehouseLog();
                    var view = composition.getWarehouseView();
                    view.addQuantity(log.getQuantityToDiff());
                }
        );
        return warehouseViewLogCompositions;
    }
}
