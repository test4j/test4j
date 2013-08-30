package org.jtester.module.jmockit.demo;

import static mockit.Mockit.setUpMock;
import static mockit.Mockit.setUpMocks;
import static mockit.Mockit.tearDownMocks;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import mockit.Mock;
import mockit.MockClass;

import org.jtester.junit.JTester;
import org.jtester.module.core.utility.MessageHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServiceATest implements JTester {

    @MockClass(realClass = Database.class)
    public static class MockDatabase {
        @Mock(invocations = 1)
        public static List<?> find(String ql, Object arg1) {
            want.string(ql).notNull();
            want.object(arg1).notNull();
            return Collections.EMPTY_LIST;
        }

        @Mock(maxInvocations = 1)
        public static void save(Object o) {
            want.object(o).notNull();
        }
    }

    @Before
    public void setUp() {
        setUpMocks(MockDatabase.class);
    }

    @After
    public void tearDown() {
        tearDownMocks();
    }

    @Test
    public void doBusinessOperationXyz() throws Exception {
        final BigDecimal total = new BigDecimal("125.40");

        MessageHelper.info("doBusinessOperationXyz-test");
        setUpMock(ServiceB.class, new Object() {
            @Mock(invocations = 1)
            public BigDecimal computeTotal(List<?> items) {
                want.collection(items).notNull();
                return total;
            }
        });

        EntityX data = new EntityX(5, "abc", "5453-1");
        new ServiceA().doBusinessOperationXyz(data);
        want.number(data.getTotal()).isEqualTo(total);
    }

    @Test(expected = Exception.class)
    public void doBusinessOperationXyzWithInvalidItemStatus() throws Exception {
        setUpMock(ServiceB.class, new Object() {
            @Mock
            public BigDecimal computeTotal(List<?> items) throws Exception {
                throw new Exception();
            }
        });

        EntityX data = new EntityX(5, "abc", "5453-1");
        new ServiceA().doBusinessOperationXyz(data);
    }
}
