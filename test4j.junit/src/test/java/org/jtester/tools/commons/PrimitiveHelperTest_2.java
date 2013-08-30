package org.jtester.tools.commons;

import mockit.Mocked;

import org.jtester.junit.JTester;
import org.junit.Test;

public class PrimitiveHelperTest_2 implements JTester {
    @Mocked
    NumberDeal deal;

    @Test
    public void getPrimitiveDefaultValue() {
        new Expectations() {
            {
                when(
                        deal.addNumber(the.integer().isEqualTo(1).wanted(), the.longnum().isEqualTo(2L).wanted(), the
                                .shortnum().isEqualTo((short) 3).wanted())).thenReturn(6);
                when(deal.setByte(the.bite().isEqualTo((byte) 0).wanted())).thenReturn(3);
            }
        };
        int ret = deal.addNumber(1, 2L, (short) 3);
        want.number(ret).isEqualTo(6);
        ret = deal.setByte((byte) 0);
        want.number(ret).isEqualTo(3);
    }

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

    public static interface NumberDeal {
        int addNumber(int i, long l, short s);

        int setByte(byte b);
    }
}
