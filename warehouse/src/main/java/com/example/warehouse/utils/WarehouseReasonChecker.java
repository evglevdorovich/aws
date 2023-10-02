package com.example.warehouse.utils;

import com.example.warehouse.config.WarehousePostfixConfig;
import com.example.warehouse.dto.WarehouseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WarehouseReasonChecker {
    private final WarehousePostfixConfig paymentPostfixConfig;

    public boolean isWithdrawal(WarehouseDto warehouse) {
        var orderId = warehouse.getOrderId();
        var postfix = paymentPostfixConfig.withdrawal();
        return orderId.endsWith(postfix);
    }
}
