package com.example.JavaSpringBoot.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    USER_EXISTED(1001, "user existed"),
    USER_NOT_FOUND(1002, "user not found"),
    KEY_INVALID(1003, "key error code invalid"),
    USERNAME_INVALID(1004, "username must be at least 6 characters"),
    PASSWORD_INVALID(1005, "password must be at least 6 characters"),
    AUTHENTICATED(1006, "authenticated"),
    CATEGORY_EXISTED(1007, "category existed"),
    CATEGORY_NOT_FOUND(1008, "category not found"),
    PRODUCT_EXISTED(1009, "product existed "),
    PRODUCT_NOT_FOUND(1010, "product not found"),
    OTHER_EXCEPTION(9999, "other exception"),
    ;
    int code;
    String message;

}
