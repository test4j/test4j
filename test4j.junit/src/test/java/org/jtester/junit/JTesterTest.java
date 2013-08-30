package org.jtester.junit;

import mockit.Mock;

import org.jtester.hamcrest.matcher.string.StringMode;
import org.jtester.module.core.CoreModule;
import org.jtester.module.core.TestListener;
import org.jtester.module.core.utility.MessageHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 整个类用于验证jTester的生命周期
 * 
 * @author darui.wudr
 */
public class JTesterTest implements JTester {
    private static StringBuffer buff = new StringBuffer();

    @BeforeClass
    public static void mockJTesterRunner() {
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
