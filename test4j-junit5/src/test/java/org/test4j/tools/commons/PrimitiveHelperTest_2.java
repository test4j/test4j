package org.test4j.tools.commons;


import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

public class PrimitiveHelperTest_2 extends Test4J {
    @Test
    public void getPrimitiveDefaultValue_2() {
        want.number((short) 3).isEqualTo(3);
        want.number((short) 3).isEqualTo((long) 3);
        want.number((short) 3).isEqualTo((short) 3);

        want.number(3).isEqualTo(3);
        want.number(3).isEqualTo((long) 3);
        want.number(3).isEqualTo((short) 3);

        want.number((long) 3).isEqualTo(3);
        want.number((long) 3).isEqualTo((long) 3);
        want.number((long) 3).isEqualTo((short) 3);

        want.number(Integer.valueOf(3)).isEqualTo(Long.valueOf(3L));
    }
}
