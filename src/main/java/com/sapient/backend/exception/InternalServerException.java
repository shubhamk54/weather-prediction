package com.sapient.backend.exception;

public class InternalServerException extends ApiException {

    public InternalServerException(String msg) {
        super(msg);
    }

    public InternalServerException(String msg, Object... params) {
        super(msg, params);
    }
}
