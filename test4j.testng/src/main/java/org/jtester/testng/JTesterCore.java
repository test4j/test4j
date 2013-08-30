package org.jtester.testng;

import java.lang.reflect.Method;
import java.util.List;

import mockit.internal.annotations.MockClassSetup;

import org.jtester.module.core.CoreModule;
import org.jtester.module.core.TestContext;
import org.jtester.module.core.utility.ListenerExecutor;
import org.jtester.testng.utility.MockTestNGMethodFinder;
import org.jtester.tools.commons.ArrayHelper;
import org.jtester.tools.commons.ExceptionWrapper;
import org.jtester.tools.commons.ListHelper;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

/**
 * @author darui.wudr
 */
@SuppressWarnings("rawtypes")
public class JTesterCore extends JTesterHookable {
    static {
        CoreModule.initSingletonInstance();
        new MockClassSetup(MockTestNGMethodFinder.class).setUpStartupMock();
    }

    @BeforeClass(alwaysRun = true)
    public void aBeforeClass(ITestContext context) {
        TestContext.setContext(this, null);
        this.dealJMockitTestDecorator(context);
        this.error_setup_class = ListenerExecutor.executeBeforeClassEvents(this.getClass());
    }

    @BeforeMethod(alwaysRun = true)
    public void aBeforeMethod(Method testedMethod) {
        this.error_setup_method = ListenerExecutor.executeBeforeMethodEvents(this, testedMethod);
    }

    @AfterMethod(alwaysRun = true)
    public void zAfterMethod(Method testedMethod) {
        Throwable throwable = ListenerExecutor.executeAfterMethodEvents(this, testedMethod);
        ExceptionWrapper.throwRuntimeException(throwable);
    }

    @AfterClass(alwaysRun = true)
    public void zAfterClass() {
        Throwable throwable = ListenerExecutor.executeAfterClassEvents(this);
        ExceptionWrapper.throwRuntimeException(throwable);
    }

    /**
     * 构造对象数组
     * 
     * @param objs
     * @return
     */
    public static Object[] toArray(Object... objs) {
        Object[] arr = ArrayHelper.toArray(objs);
        return arr;
    }

    /**
     * 构造list列表
     * 
     * @param objs
     * @return
     */
    public static List toList(Object... objs) {
        List list = ListHelper.toList(objs);
        return list;
    }
}
