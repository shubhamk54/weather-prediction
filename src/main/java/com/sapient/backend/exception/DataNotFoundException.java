package com.sapient.backend.exception;

public class DataNotFoundException extends ApiException {

    public DataNotFoundException(String msg) {
        super(msg);
    }

    public DataNotFoundException(String msg, Object... params) {
        super(msg, params);
    }
}
