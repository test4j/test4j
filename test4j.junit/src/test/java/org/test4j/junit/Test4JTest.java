package org.test4j.junit;

import mockit.Mock;
import mockit.MockUp;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.test4j.hamcrest.matcher.string.StringMode;
import org.test4j.module.core.CoreModule;
import org.test4j.module.core.TestListener;
import org.test4j.module.core.utility.MessageHelper;

/**
 * 整个类用于验证test4j的生命周期
 * 
 * @author darui.wudr
 */
public class Test4JTest extends Test4J {
    private static StringBuffer buff = new StringBuffer();

    @BeforeClass
    public static void mockTest4JRunner() {
        buff = new StringBuffer();
        new MockUp<CoreModule>() {
            @Mock
            public TestListener getTestListener() {
                return new MockListener(buff);
            }
        };
        buff.append("@B");
        MessageHelper.info("@BeforeClass");
    }

    @Test
    public void test1() {
        buff.append("TEST");
        MessageHelper.info("test1");
    }

    @Test
    public void test2() {
        buff.append("TEST");
        MessageHelper.info("test2");
    }

    static final String METHOD_BUFF_CONST = "bM bR TEST aR aM";

    @AfterClass
    public static void teardowClass() {
        buff.append("@A");
        MessageHelper.info("@AfterClass");
        want.string(buff.toString()).isEqualTo("@B bC" + METHOD_BUFF_CONST + METHOD_BUFF_CONST + "aC @A",
                StringMode.IgnoreSpace);
    }
}
