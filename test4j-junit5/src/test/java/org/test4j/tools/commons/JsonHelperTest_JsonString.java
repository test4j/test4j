package org.test4j.tools.commons;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.test4j.json.JSON;
import org.test4j.junit5.Test4J;
import org.test4j.model.Manager;

import java.util.LinkedHashMap;
import java.util.Map;

public class JsonHelperTest_JsonString extends Test4J {
    @Test
    public void fromJson() {
        String filename = "org/test4j/tools/commons/manager.json";
        Manager manager = JSONHelper.fromJsonFile(Manager.class, filename);
        want.object(manager).eqByProperties("name", "Tony Tester").eqByProperties("phoneNumber.number", "0571-88886666");
        want.date(manager.getDate()).isYear(2009).isMonth("08").isHour(16);
    }

    @Test
    public void testFromJson() {
        String json = "{\"name\": \"Banana\",\"id\": 123,\"price\": 23.0}";
        Product product = JSON.toObject(json, Product.class);
        want.object(product).eqByProperties("name", "Banana")
                .eqByProperties("id", 123);
    }

    @Data
    public static class Product {
        String name;
        int id;
        double price;

        public Product() {
            this.name = "myname";
            this.id = 100;
            this.price = 1333.00d;
        }
    }

    @Test
    public void testJsonMap() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("id", 123);
        map.put("name", "张三");

        String text = JSON.toJSON(map, false);
        want.string(text).isEqualTo("{\"id\":123,\"name\":\"张三\"}");
    }
}
