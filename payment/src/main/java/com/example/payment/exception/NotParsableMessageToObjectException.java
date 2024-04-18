package com.example.payment.exception;

public class NotParsableMessageToObjectException extends RuntimeException {
    public NotParsableMessageToObjectException() {
    }

    public NotParsableMessageToObjectException(String message) {
        super(message);
    }
}
