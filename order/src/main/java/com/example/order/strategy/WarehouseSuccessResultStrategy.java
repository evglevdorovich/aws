package com.example.order.strategy;

import com.example.order.exception.OrderNotFoundException;
import com.example.order.failover.WarehouseFailoverService;
import com.example.order.model.OrderResult;
import com.example.order.model.OrderStatus;
import com.example.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("warehouseSuccessResultStrategy")
@RequiredArgsConstructor
public class WarehouseSuccessResultStrategy implements OrderResultStrategy {
    private final OrderService orderService;
    private final WarehouseFailoverService warehouseFailoverService;
    @Override
    public void processResult(OrderResult orderResult) {
        orderService.updateWarehouseStatus(orderResult.getOrderId(), orderResult.getStatus());
        var order = orderService.findById(orderResult.getOrderId())
                .orElseThrow(OrderNotFoundException::new);
        if (order.getPaymentStatus().equals(OrderStatus.FAIL)) {
            warehouseFailoverService.onFail(order);
            orderService.sendFailoverSnsMessage("the payment failed");
        }
    }
}
