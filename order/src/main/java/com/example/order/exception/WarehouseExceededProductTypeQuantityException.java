package com.example.order.exception;

public class WarehouseExceededProductTypeQuantityException extends RuntimeException{
    public WarehouseExceededProductTypeQuantityException() {
    }

    public WarehouseExceededProductTypeQuantityException(String message) {
        super(message);
    }
}
