package com.example.awsshop.exception;

public class FileNotReadException extends RuntimeException{
    public FileNotReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
