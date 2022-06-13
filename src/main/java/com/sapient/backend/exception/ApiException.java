package com.sapient.backend.exception;

import com.sapient.backend.util.GeneralUtil;

public abstract class ApiException extends Exception {

    ApiException(String msg) {
        super(msg);
    }

    ApiException(String msg, Object... params) {
        this(GeneralUtil.formatMessage(msg, params));
    }
}
