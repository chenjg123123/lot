package com.example.service.exception;

public class RSABusinessException extends RuntimeException {
    private final int code;

    public RSABusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public RSABusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

