package org.test4j.hamcrest.iassert;

import org.test4j.function.SExecutor;
import org.test4j.hamcrest.iassert.impl.StringAssert;
import org.test4j.hamcrest.iassert.intf.IStringAssert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author darui.wu
 * @create 2020/1/19 11:59 上午
 */
public class AssertHelper {
    /**
     * 期望执行异常
     *
     * @param executor 具体执行动作
     * @param eTypes   期望执行抛出的异常类型
     * @return 异常错误String断言
     */
    public static IStringAssert exception(SExecutor executor, Class<? extends Throwable>... eTypes) {
        List<Class> wanted = new ArrayList<>();
        if (eTypes == null || eTypes.length == 0) {
            wanted.add(Exception.class);
        } else {
            Arrays.stream(eTypes).forEach(wanted::add);
        }
        String expected = wanted.stream().map(Class::getName).collect(Collectors.joining(","));
        try {
            executor.doIt();
            throw new UnExpectedAssertionError("Expected exception: [" + expected + "], but no exception was thrown.");
        } catch (UnExpectedAssertionError ue) {
            throw ue;
        } catch (Throwable e) {
            boolean matched = false;
            for (Class expectedType : eTypes) {
                if (expectedType.isAssignableFrom(e.getClass())) {
                    matched = true;
                    break;
                }
            }
            if (matched) {
                return new StringAssert<>(e.getMessage());
            } else {
                throw new AssertionError("Expected exception: [" + expected + "], but actual exception is: " + e.getClass());
            }
        }
    }

    private static class UnExpectedAssertionError extends AssertionError {
        public UnExpectedAssertionError(String error) {
            super(error);
        }
    }
}
