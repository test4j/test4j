package org.jtester.tools.commons;

import java.lang.reflect.Field;
import java.util.List;

import org.jtester.fortest.beans.Manager;
import org.jtester.junit.JTester;
import org.jtester.junit.annotations.DataFrom;
import org.jtester.tools.exception.NoSuchFieldRuntimeException;
import org.junit.Test;

public class FieldHelperTest implements JTester {

    @Test
    @DataFrom("getFieldData")
    public void testGetField(String fieldname, String value) throws IllegalArgumentException, IllegalAccessException {
        Object target = new ChildClaz();
        Field field = FieldHelper.getField(ChildClaz.class, fieldname);
        field.setAccessible(true);
        String result = (String) field.get(target);

        want.string(result).isEqualTo(value);
    }

    @Test(expected = NoSuchFieldRuntimeException.class)
    public void testGetField_NoSuchField() {
        FieldHelper.getField(ChildClaz.class, "no_such_field");
    }

    public static Object[][] getFieldData() {
        return new Object[][] { { "static_field", "ChildClaz" }, // <br>
                { "public_field", "ChildClaz" }, // <br>
                { "private_field", "ChildClaz" }, // <br>

                { "parent_static_field", "ParentClaz" }, // <br>
                { "parent_public_field", "ParentClaz" }, // <br>
                { "parent_private_field", "ParentClaz" } // <br>
        };
    }

    @SuppressWarnings("unused")
    public static class ParentClaz {
        static String  static_field         = "ParentClaz";

        public String  public_field         = "ParentClaz";

        private String private_field        = "ParentClaz";
        // only parent have
        static String  parent_static_field  = "ParentClaz";

        public String  parent_public_field  = "ParentClaz";

        private String parent_private_field = "ParentClaz";
    }

    @SuppressWarnings("unused")
    public static class ChildClaz extends ParentClaz {
        static String  static_field  = "ChildClaz";

        public String  public_field  = "ChildClaz";

        private String private_field = "ChildClaz";
    }

    @Test
    public void testGetAllFields() {
        List<Field> fields = ClazzHelper.getAllFields(Manager.class, null, false, false, false);
        want.collection(fields).sizeEq(4)
                .propertyEq("name", new String[] { "secretary", "phoneNumber", "name", "date" });
    }
}
