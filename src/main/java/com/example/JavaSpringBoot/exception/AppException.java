package com.example.JavaSpringBoot.exception;

public class AppException extends RuntimeException {

  private ErrorCode errorCode;

  public AppException(String message) {
    super(message);
  }

  public AppException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }

}
