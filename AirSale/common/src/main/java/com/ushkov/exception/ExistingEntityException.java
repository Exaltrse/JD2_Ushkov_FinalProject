package com.ushkov.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExistingEntityException extends RuntimeException {
    public ExistingEntityException() {
        super();
    }
    public ExistingEntityException(String message, Throwable cause) {
        super(message, cause);
    }
    public ExistingEntityException(String message) {
        super(message);
    }
    public ExistingEntityException(Throwable cause) {
        super(cause);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public static final class Cause{
        public static final String ALREADY_EXIST = "Such entity already exist.";
    }
}