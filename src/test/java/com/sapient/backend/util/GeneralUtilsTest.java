package com.sapient.backend.util;


import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class GeneralUtilsTest {

    @Test
    public void testFormatMessageWithParams() {

        String msg = "This is {} parameter, with value: {}";
        String formatted = GeneralUtil.formatMessage(msg, new Object[]{"first", 30});
        Assertions.assertEquals("This is first parameter, with value: 30", formatted);
    }

    @Test
    public void testFormatMessageWithoutParams() {

        String msg = "This is a test text without any params";
        String formatted = GeneralUtil.formatMessage(msg);
        Assertions.assertEquals("This is a test text without any params", formatted);
    }
}
