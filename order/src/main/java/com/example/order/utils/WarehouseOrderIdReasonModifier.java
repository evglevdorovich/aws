package com.example.order.utils;

import com.example.order.config.WarehousePostfixConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WarehouseOrderIdReasonModifier {
    private final WarehousePostfixConfig warehousePostfixConfig;
    public String modifyForWithdrawal(String orderId) {
        return orderId + warehousePostfixConfig.withdrawal();
    }
    public String modifyForCancel(String orderId) {
        return orderId + warehousePostfixConfig.cancel();
    }

}
