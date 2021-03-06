package com.ushkov.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchEntityException extends RuntimeException {
    public NoSuchEntityException() {
        super("There are no such entity in DB.");
    }
    public NoSuchEntityException(String message, Throwable cause) {
        super(message, cause);
    }
    public NoSuchEntityException(String message) {
        super(message);
    }
    public NoSuchEntityException(long id, String tableName) {
        super(Cause.NO_SUCH_ID + id + " in table " + tableName + ".");
    }

    public NoSuchEntityException(long id) {
        super(Cause.NO_SUCH_ID + id + " .");
    }


    public NoSuchEntityException(Throwable cause) {
        super(cause);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public static final class Cause{
        public static final String NO_SUCH_ID = "There are no such entity in DB whith ID = ";
    }
}
