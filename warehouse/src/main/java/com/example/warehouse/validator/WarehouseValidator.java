package com.example.warehouse.validator;

import com.example.warehouse.exception.NotEnoughAmountOfProductsOnWarehouse;
import com.example.warehouse.model.WarehouseView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class WarehouseValidator {
    public void validateWarehouseViews(Set<WarehouseView> views)  {
        views.forEach(this::validateView);
    }

    private void validateView(WarehouseView warehouseView) {
        if (warehouseView.getQuantity() < 0) {
            log.warn("product with id = {}, has insufficient quantity",
                    warehouseView.getProductId());
            throw new NotEnoughAmountOfProductsOnWarehouse();
        }
    }
}
