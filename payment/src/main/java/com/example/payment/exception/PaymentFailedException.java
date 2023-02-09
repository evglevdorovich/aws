package com.example.payment.exception;

public class PaymentFailedException extends RuntimeException{
    public PaymentFailedException() {
    }

    public PaymentFailedException(String message) {
        super(message);
    }
}
