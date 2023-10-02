package com.example.order.strategy;

import com.example.order.exception.OrderNotFoundException;
import com.example.order.failover.PaymentFailoverService;
import com.example.order.model.OrderResult;
import com.example.order.model.OrderStatus;
import com.example.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("warehouseFailResultStrategy")
@RequiredArgsConstructor
public class WarehouseFailResultStrategy implements OrderResultStrategy {
    private final OrderService orderService;
    private final PaymentFailoverService paymentFailoverService;

    @Override
    public void processResult(OrderResult orderResult) {
        orderService.updateWarehouseStatus(orderResult.getOrderId(), OrderStatus.FAIL);
        var order = orderService.findById(orderResult.getOrderId())
                .orElseThrow(OrderNotFoundException::new);

        if (order.getPaymentStatus().equals(OrderStatus.FAIL)) {
            orderService.sendFailoverSnsMessage("the payment and warehouse are failed");
        }
        else if(order.getPaymentStatus().equals(OrderStatus.SUCCESS)) {
            paymentFailoverService.onFail(order);
            orderService.sendFailoverSnsMessage("the payment is failed");
        }
    }
}
