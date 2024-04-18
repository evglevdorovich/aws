package com.example.order.utils;

import com.example.order.model.OrderResult;
import org.springframework.stereotype.Component;

@Component
public class OrderReasonExtractor {
    public OrderResult extractReason(OrderResult orderResult){
        var orderIdWithReason = orderResult.getOrderId();
        var orderIdWithoutReason = orderIdWithReason.substring(0, orderIdWithReason.indexOf("_"));
        orderResult.setOrderId(orderIdWithoutReason);
        return orderResult;
    }

}
