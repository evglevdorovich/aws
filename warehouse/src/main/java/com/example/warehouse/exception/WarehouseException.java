package com.example.warehouse.exception;

public class WarehouseException extends RuntimeException {
    public WarehouseException() {
    }

    public WarehouseException(String message) {
        super(message);
    }

    public WarehouseException(String message, Throwable cause) {
        super(message, cause);
    }
}
