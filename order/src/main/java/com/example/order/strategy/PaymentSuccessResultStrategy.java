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
@Qualifier("paymentSuccessResultStrategy")
@RequiredArgsConstructor
public class PaymentSuccessResultStrategy implements OrderResultStrategy {
    private final OrderService orderService;
    private final PaymentFailoverService paymentFailoverService;

    @Override
    public void processResult(OrderResult orderResult) {
        orderService.updatePaymentStatus(orderResult.getOrderId(), orderResult.getStatus());
        var order = orderService.findById(orderResult.getOrderId())
                .orElseThrow(OrderNotFoundException::new);
        if (order.getWarehouseStatus().equals(OrderStatus.FAIL)) {
            paymentFailoverService.onFail(order);
            orderService.sendFailoverSnsMessage("the warehouse is failed");
        }
    }
}
