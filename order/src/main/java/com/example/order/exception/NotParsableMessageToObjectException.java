package com.example.order.exception;

public class NotParsableMessageToObjectException extends RuntimeException {
    public NotParsableMessageToObjectException() {
    }

    public NotParsableMessageToObjectException(String message) {
        super(message);
    }

    public NotParsableMessageToObjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
