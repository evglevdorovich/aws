package com.example.payment.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaymentResultFormatException extends PaymentFailedException {
    public PaymentResultFormatException(String message) {
    }
}
