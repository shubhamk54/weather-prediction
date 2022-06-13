package com.sapient.backend.util;

public class GeneralUtil {

    private GeneralUtil() {
    }

    public static String formatMessage(String msg, Object... params) {
        return params != null && params.length > 0 ? String.format(msg.replace("{}", "%s"), params) : msg;
    }
}
