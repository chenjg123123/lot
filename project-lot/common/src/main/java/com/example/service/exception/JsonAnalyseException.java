package com.example.service.exception;

public class JsonAnalyseException extends RuntimeException{
    private final  int code;

    public JsonAnalyseException(int code, String message) {
        super(message);
        this.code = code;
    }
    public int getCode() {
        return code;
    }
    public JsonAnalyseException(String message) {
        super(message);
        this.code = 500;
    }
}
