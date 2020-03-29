package org.test4j.tools.commons;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.exception.NoSuchFieldRuntimeException;
import org.test4j.junit5.Test4J;
import org.test4j.model.Manager;

public class FieldHelperTest extends Test4J {

    @ParameterizedTest
    @MethodSource("getFieldData")
    public void testGetField(String fieldname, String value) throws IllegalArgumentException, IllegalAccessException {
        Object target = new ChildClaz();
        Field field = Reflector.getField(ChildClaz.class, fieldname);
        field.setAccessible(true);
        String result = (String) field.get(target);

        want.string(result).isEqualTo(value);
    }

    @Test
    public void testGetField_NoSuchField() {
        want.exception(() ->
                        Reflector.getField(ChildClaz.class, "no_such_field")
                , NoSuchFieldRuntimeException.class);
    }

    public static Object[][] getFieldData() {
        return new Object[][]{{"static_field", "ChildClaz"}, // <br>
                {"public_field", "ChildClaz"}, // <br>
                {"private_field", "ChildClaz"}, // <br>

                {"parent_static_field", "ParentClaz"}, // <br>
                {"parent_public_field", "ParentClaz"}, // <br>
                {"parent_private_field", "ParentClaz"} // <br>
        };
    }

    @SuppressWarnings("unused")
    public static class ParentClaz {
        static String static_field = "ParentClaz";

        public String public_field = "ParentClaz";

        private final String private_field = "ParentClaz";
        // only parent have
        static String parent_static_field = "ParentClaz";

        public String parent_public_field = "ParentClaz";

        private final String parent_private_field = "ParentClaz";
    }

    @SuppressWarnings("unused")
    public static class ChildClaz extends ParentClaz {
        static String static_field = "ChildClaz";

        public String public_field = "ChildClaz";

        private final String private_field = "ChildClaz";
    }

    @Test
    public void testGetAllFields() {
        List<Field> fields = ClazzHelper.getAllFields(Manager.class, null, false, false, false);
        want.collection(fields).sizeEq(4)
                .eqByProperties("name", new String[]{"secretary", "phoneNumber", "name", "date"});
    }
}
