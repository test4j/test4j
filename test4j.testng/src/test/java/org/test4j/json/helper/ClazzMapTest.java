package org.test4j.json.helper;

import org.test4j.json.encoder.beans.test.User;
import org.test4j.testng.Test4J;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = { "test4j", "json" })
public class ClazzMapTest extends Test4J {

    @Test(dataProvider = "type_data")
    public void testGetClazzName(Object obj, String expected) {
        String clazzname = ClazzMap.getClazzName(obj);
        want.string(clazzname).start(expected);
    }

    @DataProvider
    public Object[][] type_data() {
        return new Object[][] { { Integer.valueOf(1), "Integer" }, // <br>
                { new int[1], "int[]@" }, // <br>
                { new Integer[0], "Integer[]@" }, // <br>
                { new String(), "string" }, // <br>
                { new String[0], "string[]@" }, // <br>
                { new User(), "org.test4j.json.encoder.beans.test.User@" }, // <br>
                { new User[0], "[Lorg.test4j.json.encoder.beans.test.User;@" } // <br>
        };
    }

    public void testGetClazzName_Primitive() {
        String clazzname = ClazzMap.getClazzName(1);
        want.string(clazzname).isEqualTo("Integer");
    }
}
