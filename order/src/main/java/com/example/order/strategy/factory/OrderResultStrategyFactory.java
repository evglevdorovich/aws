package com.example.order.strategy.factory;

import com.example.order.model.OrderResult;
import com.example.order.model.OrderStatus;
import com.example.order.strategy.OrderResultStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class OrderResultStrategyFactory {
    public static final String CANNOT_FIND_BEAN_EXCEPTION_MESSAGE = "don't have specific strategy for this source and status";
    private static final String PAYMENT_SOURCE_NAME = "payment";
    private static final String WAREHOUSE_SOURCE_NAME = "warehouse";
    private static final String PAYMENT_SUCCESS_BEAN_NAME = "paymentSuccessResultStrategy";
    private static final String PAYMENT_FAIL_BEAN_NAME = "paymentFailResultStrategy";
    private static final String WAREHOUSE_SUCCESS_BEAN_NAME = "warehouseSuccessResultStrategy";
    private static final String WAREHOUSE_FAIL_BEAN_NAME = "warehouseFailResultStrategy";
    private final Map<String, OrderResultStrategy> orderResultStrategies;

    public OrderResultStrategy getStrategy(OrderResult orderResult) {
        var source = orderResult.getSource();
        var status = orderResult.getStatus();

        if (source.equals(PAYMENT_SOURCE_NAME)) {
            return getPaymentResultStrategy(status);
        } else if (source.equals(WAREHOUSE_SOURCE_NAME)) {
            return getWarehouseResultStrategy(status);
        } else {
            throw new IllegalArgumentException(CANNOT_FIND_BEAN_EXCEPTION_MESSAGE);
        }
    }

    private OrderResultStrategy getWarehouseResultStrategy(OrderStatus status) {
        if (status.equals(OrderStatus.SUCCESS)) {
            return orderResultStrategies.get(WAREHOUSE_SUCCESS_BEAN_NAME);
        } else if (status.equals(OrderStatus.FAIL)) {
            return orderResultStrategies.get(WAREHOUSE_FAIL_BEAN_NAME);
        } else {
            throw new IllegalArgumentException(CANNOT_FIND_BEAN_EXCEPTION_MESSAGE);
        }
    }

    private OrderResultStrategy getPaymentResultStrategy(OrderStatus status) {
        if (status.equals(OrderStatus.SUCCESS)) {
            return orderResultStrategies.get(PAYMENT_SUCCESS_BEAN_NAME);
        } else if (status.equals(OrderStatus.FAIL)) {
            return orderResultStrategies.get(PAYMENT_FAIL_BEAN_NAME);
        } else {
            throw new IllegalArgumentException(CANNOT_FIND_BEAN_EXCEPTION_MESSAGE);
        }
    }
}
