package com.example.order.exception;

public class PaymentFormatException extends RuntimeException {
    public PaymentFormatException() {
    }

    public PaymentFormatException(String message) {
        super(message);
    }
}
