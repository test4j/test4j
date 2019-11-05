package org.test4j.junit;


import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.test4j.module.core.internal.Test4JContext;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;
import static org.test4j.module.core.CoreModule.getTestListener;

/**
 * JUnit框架方法装饰
 */
public class JUnit4Helper {

    /**
     * 对junit childrenInvoker方法进行装饰
     *
     * @param supplier
     * @return
     */
    public static Statement childrenInvoker(Supplier<Statement> supplier) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                getTestListener().beforeClass(Test4JContext.currTestedClazz());
                supplier.get().evaluate();
                getTestListener().afterClass(Test4JContext.currTestedObject());
            }
        };
    }

    /**
     * 对junit的methodInvoker方法进行修饰
     *
     * @param method
     * @param supplier
     * @return
     */
    public static Statement methodInvoker(FrameworkMethod method, Supplier<Statement> supplier) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Throwable methodE = null;
                try {
                    getTestListener().beforeMethod(Test4JContext.currTestedObject(), method.getMethod());
                    supplier.get().evaluate();
                } catch (Throwable e) {
                    methodE = e;
                    throw e;
                } finally {
                    getTestListener().afterMethod(Test4JContext.currTestedObject(), method.getMethod(), methodE);
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
    public static List<FrameworkMethod> computeTestMethods(Supplier<List<FrameworkMethod>> supplier) {
        return supplier.get().stream()
                .map(JUnit4Helper::withParameter)
                .flatMap(list -> list.stream())
                .collect(toList());
    }

    private static List<FrameworkMethod> withParameter(FrameworkMethod method) {
        DataFrom dataFrom = method.getMethod().getAnnotation(DataFrom.class);
        if (dataFrom == null) {
            return Arrays.asList(method);
        } else {
            return ParameterDataFromHelper.computeParameterizedTestMethods(Test4JContext.currTestedClazz(), method.getMethod(), dataFrom);
        }
    }
}
