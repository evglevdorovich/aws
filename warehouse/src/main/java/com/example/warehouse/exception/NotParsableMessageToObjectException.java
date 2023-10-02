package com.example.warehouse.exception;

public class NotParsableMessageToObjectException extends WarehouseException {
    public NotParsableMessageToObjectException() {
    }

    public NotParsableMessageToObjectException(String message) {
        super(message);
    }
}
