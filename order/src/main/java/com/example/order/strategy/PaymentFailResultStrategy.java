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
@Qualifier("paymentFailResultStrategy")
@RequiredArgsConstructor
public class PaymentFailResultStrategy implements OrderResultStrategy {
    private final OrderService orderService;
    private final WarehouseFailoverService warehouseFailoverService;
    @Override
    public void processResult(OrderResult orderResult) {
        orderService.updatePaymentStatus(orderResult.getOrderId(), OrderStatus.FAIL);
        var order = orderService.findById(orderResult.getOrderId())
                .orElseThrow(OrderNotFoundException::new);

        if (order.getWarehouseStatus().equals(OrderStatus.FAIL)) {
            orderService.sendFailoverSnsMessage("the payment and warehouse are failed");
        }
        else if(order.getWarehouseStatus().equals(OrderStatus.SUCCESS)) {
            warehouseFailoverService.onFail(order);
            orderService.sendFailoverSnsMessage("the warehouse is failed");
        }
    }
}
