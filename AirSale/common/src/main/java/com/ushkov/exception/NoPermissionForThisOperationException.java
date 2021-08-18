package com.ushkov.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NoPermissionForThisOperationException extends RuntimeException{
    public NoPermissionForThisOperationException() {
        super("Not enough permissions for this operation.");
    }
    public NoPermissionForThisOperationException(String message, Throwable cause) {
        super(message, cause);
    }
    public NoPermissionForThisOperationException(String message) {
        super(message);
    }
    public NoPermissionForThisOperationException(Throwable cause) {
        super(cause);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
