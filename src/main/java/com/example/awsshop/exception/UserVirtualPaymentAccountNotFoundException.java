package com.example.awsshop.exception;

public class UserVirtualPaymentAccountNotFoundException extends RuntimeException{
    public UserVirtualPaymentAccountNotFoundException() {
    }

    public UserVirtualPaymentAccountNotFoundException(String message) {
        super(message);
    }
}
