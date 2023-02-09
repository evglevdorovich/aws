package com.example.order.exception;

public class CannotFindIamRoleCredentials extends RuntimeException{
    public CannotFindIamRoleCredentials() {
    }

    public CannotFindIamRoleCredentials(String message) {
        super(message);
    }
}
