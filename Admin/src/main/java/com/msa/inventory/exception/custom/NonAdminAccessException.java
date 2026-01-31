package com.msa.inventory.exception.custom;

public class NonAdminAccessException extends Exception {
    String message;

    public NonAdminAccessException(String message) {
        super(message);
    }
}
