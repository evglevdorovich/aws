package com.example.order.exception;

public class NotSufficientProductsInWarehouseException extends PaymentFailedException {

    public NotSufficientProductsInWarehouseException() {
    }

    public NotSufficientProductsInWarehouseException(String message) {
        super(message);
    }
}
