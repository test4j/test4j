package org.test4j.junit;


import lombok.Setter;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.test4j.module.core.utility.ClazzAroundObject;
import org.test4j.module.core.internal.Test4JTestContext;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;
import static org.test4j.module.core.CoreModule.getTestListener;

/**
 * JUnit框架方法装饰
 */
public class JUnitTestContext {
    private Class testedClass;

    @Setter
    private Object testedObject;

    private RuntimeException error;

    public JUnitTestContext(Class testedClass) {
        this.testedClass = testedClass;
        Test4JTestContext.setContext(new ClazzAroundObject.ClazzBeforeObject(testedClass), null);
    }

    /**
     * 对junit childrenInvoker方法进行装饰
     *
     * @param supplier
     * @return
     */
    public Statement childrenInvoker(Supplier<Statement> supplier) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                getTestListener().beforeClass(testedClass);
                supplier.get().evaluate();
                getTestListener().afterClass(testedObject);
            }
        };
    }

    /**
     * 在setup方法前执行
     *
     * @param method
     */
    public void runBeforeSetup(final FrameworkMethod method) {
        getTestListener().beforeSetup(testedObject, method.getMethod());
    }

    /**
     * 对junit的methodInvoker方法进行修饰
     *
     * @param method
     * @param supplier
     * @return
     */
    public Statement methodInvoker(FrameworkMethod method, Supplier<Statement> supplier) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Throwable methodE = null;
                try {
                    getTestListener().beforeMethod(testedObject, method.getMethod());
                    supplier.get().evaluate();
                } catch (Throwable e) {
                    methodE = e;
                    throw e;
                } finally {
                    getTestListener().afterMethod(testedObject, method.getMethod(), methodE);
                }
            }
        };
    }

    /**
     * 将junit测试方法转换为test4j测试方法
     *
     * @param supplier
     * @return
     */
    public List<FrameworkMethod> computeTestMethods(Supplier<List<FrameworkMethod>> supplier) {
        return supplier.get().stream()
                .map(this::withParameter)
                .flatMap(list -> list.stream())
                .collect(toList());
    }

    private List<FrameworkMethod> withParameter(FrameworkMethod method) {
        DataFrom dataFrom = method.getMethod().getAnnotation(DataFrom.class);
        if (dataFrom == null) {
            return Arrays.asList(method);
        } else {
            return ParameterDataFromHelper.computeParameterizedTestMethods(testedClass, method.getMethod(), dataFrom);
        }
    }
}
