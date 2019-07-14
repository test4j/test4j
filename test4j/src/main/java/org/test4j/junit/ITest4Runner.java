package org.test4j.junit;

import org.junit.Test;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.lang.annotation.Annotation;
import java.util.List;

public interface ITest4Runner {
    /**
     * 封装测试类
     *
     * @param testClass
     * @return
     */
    TestClass createTestClass(Class<?> testClass);

    /**
     * 创建测试对象实例
     *
     * @return
     * @throws Exception
     */
    Object createTest() throws Exception;

    /**
     * 调用测试方法
     *
     * @param notifier
     * @return
     */
    Statement childrenInvoker(final RunNotifier notifier);

    /**
     * 调用测试方法
     *
     * @param method
     * @param testedObject
     * @return
     */
    Statement methodInvoker(FrameworkMethod method, Object testedObject);

    /**
     * 计算（遍历）测试方法
     *
     * @return
     */
    List<FrameworkMethod> computeTestMethods();

    /**
     * 验证测试方法有效性
     *
     * @param annotation
     * @param isStatic
     * @param errors
     */
    void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation, boolean isStatic, List<Throwable> errors);

    /**
     * 验证测试方法有效性
     *
     * @param annotation
     * @param isStatic
     * @param errors
     */
    default void validateMethod(Class<? extends Annotation> annotation, boolean isStatic, List<Throwable> errors) {
        List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(annotation);
        if (annotation.isAssignableFrom(Test.class)) {
            methods = this.computeTestMethods();
        }
        methods.forEach(method -> method.validatePublicVoidNoArg(isStatic, errors));
    }

    /**
     * 返回SpringJUnit4ClassRunner 中构造的TestClass
     *
     * @return
     */
    TestClass getTestClass();
}
