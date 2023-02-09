package com.example.warehouse.utils;

import com.example.warehouse.config.WarehousePostfixConfig;
import com.example.warehouse.dto.WarehouseDto;
import com.example.warehouse.model.WarehouseLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.negateExact;

@Component
@RequiredArgsConstructor
public class WarehouseDtoConverter {
    private final WarehousePostfixConfig warehousePostfixConfig;
    public Set<WarehouseLog> convertToLogs(WarehouseDto warehouseDto){
        Set<WarehouseLog> warehouseLogs = new HashSet<>();

        boolean isWithdrawal = isWithdrawal(warehouseDto);

        for(var productDto : warehouseDto.getProducts()) {
            var quantity = productDto.getQuantity();
            var amountToDiff = isWithdrawal ? negateExact(quantity) : quantity;
            var wareHouseLog = new WarehouseLog(productDto.getId(), warehouseDto.getOrderId(), amountToDiff);
            warehouseLogs.add(wareHouseLog);
        }
        return warehouseLogs;
    }

    private boolean isWithdrawal(WarehouseDto warehouseDto) {
        var withdrawalPostfix = warehousePostfixConfig.withdrawal();
        return warehouseDto.getOrderId().endsWith(withdrawalPostfix);
    }
}
