package org.test4j.testng.database.annotations;

import java.lang.reflect.Method;

import org.test4j.testng.Test4J;
import org.test4j.testng.utility.TestNgUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = "test4j")
public class TestNgUtilTest extends Test4J {

    @Test(dataProvider = "getMethods")
    public void testDoesIsTestMethod(String methodName, boolean isTestMethod) throws SecurityException,
            NoSuchMethodException {
        Method method = TestNgUtilTestHelper.class.getDeclaredMethod(methodName);
        boolean is = TestNgUtil.isTestMethod(TestNgUtilTestHelper.class, method);
        want.object(is).isEqualTo(isTestMethod);
    }

    @DataProvider
    public Object[][] getMethods() {
        return new Object[][] { { "testHasTest", true },// <br>
                { "testNotTest", true }, // <br>
                { "testNotTest_ButHasOtherAnnotation", true }, // <br>
                { "beforeTest", false },// <br>
                { "staticMethod", true }, // <br>
                { "privateMethod", false }, // <br>
                { "dataProvier", false } // <br>
        };
    }

    @Test
    public void testDoesIsTestMethod2() throws SecurityException, NoSuchMethodException {
        Method method = TestNgUtilTestHelper2.class.getDeclaredMethod("isTestMethod");
        boolean is = TestNgUtil.isTestMethod(TestNgUtilTestHelper2.class, method);
        want.object(is).isEqualTo(true);
    }
}
