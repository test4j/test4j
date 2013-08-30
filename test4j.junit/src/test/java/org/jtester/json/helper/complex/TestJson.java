package org.jtester.json.helper.complex;

import java.util.ArrayList;

import org.jtester.junit.JTester;
import org.junit.Test;
import org.test4j.hamcrest.matcher.modes.ItemsMode;
import org.test4j.json.JSON;
import org.test4j.json.helper.JSONFeature;
import org.test4j.tools.commons.DateHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TestJson implements JTester {
    @Test
    public void testList() {
        ComplexPoJo dbpojo = new ComplexPoJo();
        dbpojo.setDateValue(DateHelper.parse("2011-08-19 16:04:38"));
        ArrayList list = new ArrayList();
        list.add(dbpojo);

        String jtestJson = JSON.toJSON(list, JSONFeature.QuoteAllItems, JSONFeature.SkipNullValue);
        System.out.println(jtestJson);
        ArrayList obj = JSON.toObject(jtestJson);
        want.collection(obj).notNull()
                .propertyMatch(ItemsMode.AnyItems, "dateValue", the.date().eqByFormat("2011-08-19"));
    }
}
