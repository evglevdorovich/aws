package com.example.order.validator;

import com.example.order.config.WarehouseConfig;
import com.example.order.dto.WarehouseDto;
import com.example.order.exception.WarehouseExceededProductTypeQuantityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WarehouseDtoValidator {
    private final WarehouseConfig warehouseConfig;

    //should be done on warehouse service also
    // the main reason of - to decrease the amount of conflicts while performing insert with optimal locking
    // and the limitation for 25 in 1 transaction per request, cause we have view and log -> 2 inserts for each and = 12 max
    public void validation(WarehouseDto warehouseDto) {
        var productTypeQuantity = warehouseDto.getProducts().size();
        var productTypeLimit = warehouseConfig.productTypeLimit();
        if (productTypeQuantity > productTypeLimit) {
            throw new WarehouseExceededProductTypeQuantityException(
                    String.format("Should be less or equals to %s but was : %s", productTypeLimit,productTypeQuantity));
        }
    }
}
