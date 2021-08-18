package com.ushkov.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TimestampException extends RuntimeException{

    public TimestampException(String message) {
        super(message);
    }

    public TimestampException(String firstField, String secondField) {
        super("Value of " + firstField + " must be less than " + secondField + ".");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
