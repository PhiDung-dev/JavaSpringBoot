package com.example.JavaSpringBoot.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {

    KEY_INVALID(1001, "key error code invalid", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1002, "username must be at least 6 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003, "password must be at least 6 characters", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1004, "user existed", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1005, "user not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1010, "authenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1011, "unauthorized", HttpStatus.FORBIDDEN),
    OTHER_EXCEPTION(9999, "other exception", HttpStatus.INTERNAL_SERVER_ERROR),
    PERMISSION_EXISTED(9998, "permission existed", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_FOUND(9997, "permission not found", HttpStatus.NOT_FOUND),
    ROLE_EXISTED(9996, "role existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(9995, "role not found", HttpStatus.NOT_FOUND),
    ;
    int code;
    String message;
    HttpStatusCode statusCode;

}
