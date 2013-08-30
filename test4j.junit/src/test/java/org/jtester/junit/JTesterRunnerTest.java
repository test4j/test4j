package org.jtester.junit;

import java.lang.reflect.Method;

import mockit.Mock;

import org.jtester.hamcrest.matcher.string.StringMode;
import org.jtester.module.ICore;
import org.jtester.module.core.CoreModule;
import org.jtester.module.core.TestListener;
import org.jtester.module.core.utility.MessageHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import forfilter.DaveyCategory;

/**
 * 整个类用于验证jTester的生命周期
 * 
 * @author darui.wudr
 */
@Category(DaveyCategory.class)
public class JTesterRunnerTest implements ICore {
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

class MockListener extends TestListener {
    static StringBuffer buff;

    public MockListener(StringBuffer buff) {
        MockListener.buff = buff;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void beforeClass(Class testClazz) {
        buff.append("bC");
        MessageHelper.info("setupClass");
    }

    @Override
    public void beforeMethod(Object testObject, Method testMethod) {
        buff.append("bM");
        MessageHelper.info("setupMethod");
    }

    @Override
    public void beforeRunning(Object testObject, Method testMethod) {
        buff.append("bR");
        MessageHelper.info("beforeMethodRunning");
    }

    @Override
    public void afterRunned(Object testObject, Method testMethod, Throwable testThrowable) {
        buff.append("aR");
        MessageHelper.info("afterMethodRunned");
    }

    @Override
    public void afterMethod(Object testObject, Method testMethod) {
        buff.append("aM");
        MessageHelper.info("teardownMethod");
    }

    @Override
    public void afterClass(Object testObject) {
        buff.append("aC");
        MessageHelper.info("teardownClass");
    }

    @Override
    protected String getName() {
        return "mock test listener";
    }
}
