package org.test4j.json;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.tools.commons.DateHelper;

import java.util.Date;

public class JsonTest extends Test4J {
    @Test
    void test_date_json() {
        Date date = DateHelper.parse("2020-04-11 12:04:05");
        String info = JSON.toJSON(date, true);
        MessageHelper.info(info);
        info = JSON.toJSON("2020-04-11 12:04:05", true);
        MessageHelper.info(info);
    }
}
