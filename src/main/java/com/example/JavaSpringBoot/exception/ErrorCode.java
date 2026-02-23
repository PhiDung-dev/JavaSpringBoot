package com.example.JavaSpringBoot.exception;

public enum ErrorCode {
    USER_EXISTED(1001, "user existed"),
    USER_NOT_FOUND(1002, "user not found"),
    OTHER_EXCEPTION(9999, "other exception"),
    USERNAME_INVALID(1003, "username must be at least 6 characters"),
    PASSWORD_INVALID(1004, "password must be at least 6 characters"),
    KEY_INVALID(1005, "key error code invalid")
    ;
    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
