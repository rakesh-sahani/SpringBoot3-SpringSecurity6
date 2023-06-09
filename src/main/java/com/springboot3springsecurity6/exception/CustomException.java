package com.springboot3springsecurity6.exception;

public class CustomException extends Exception {

    private int status;
    private int code;
    private String message;
    private static final long serialVersionUID = 4134530760765747944L;

    public CustomException(int status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return this.status;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}