package org.test4j.junit4;

import org.junit.runners.model.FrameworkMethod;
import org.test4j.exception.NewInstanceException;
import org.test4j.exception.NoSuchMethodRuntimeException;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.tools.commons.MethodHelper;
import org.test4j.tools.commons.Reflector;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ParameterDataFromHelper {
    /**
     * 构造一系列有参的测试方法
     *
     * @param testClazz  当前测试类class
     * @param testMethod 测试方法
     * @param dataFrom
     * @return
     */
    public static List<FrameworkMethod> computeParameterizedTestMethods(Class testClazz,
                                                                        Method testMethod,
                                                                        DataFrom dataFrom) {
        String fromMethod = dataFrom.value();
        if ("".equals(fromMethod)) {
            throw new RuntimeException("You should specify the value property of @DataFrom() item.");
        }
        switch (dataFrom.source()) {
            case FromMethod:
                Class dataFromClaz = dataFrom.clazz();
                if (dataFromClaz == DataFrom.class) {
                    dataFromClaz = testMethod.getDeclaringClass();
                }
                return computeParameterziedFromDataProviderMethod(testClazz, testMethod, fromMethod, dataFromClaz);
            case FromFile:
                // TODO
            default:
                throw new RuntimeException("umimplement the data from uri mode.");
        }
    }

    private static List<FrameworkMethod> computeParameterziedFromDataProviderMethod(Class testClazz,
                                                                                    Method testMethod,
                                                                                    String dataFromMethod,
                                                                                    Class dataFromClaz) {
        Object data = getDataFromMethod(dataFromMethod, testClazz, dataFromClaz);
        if (data instanceof Iterator) {
            return computeParameterFromIterator(testMethod, (Iterator) data);
        } else if (data instanceof Object[][]) {
            return computeParameterFromArray(testMethod, (Object[][]) data);
        } else {
            throw new RuntimeException(
                    "The @DataFrom method can only return value of type Iterator<Object[]> or Object[][].");
        }
    }

    private static Object getDataFromMethod(String dataFromMethod, Class testClazz, Class dataFromClaz) {
        try {
            if (dataFromClaz.isAssignableFrom(testClazz)) {
                Object from = Reflector.instance.newInstance(testClazz);
                Object data = MethodHelper.invokeUnThrow(from, dataFromMethod);
                return data;
            } else {
                Object from = Reflector.instance.newInstance(dataFromClaz);
                Object data = MethodHelper.invokeUnThrow(from, dataFromMethod);
                return data;
            }
        } catch (NewInstanceException e1) {
            try {
                Object data = MethodHelper.invokeStatic(dataFromClaz, dataFromMethod);
                return data;
            } catch (NoSuchMethodRuntimeException e2) {
                String err = "The @DataFrom method isn't a static method or isn't declared in a concrete class.";
                MessageHelper.error(err, e1, e2);
                throw new RuntimeException(err, e2);
            }
        }
    }

    private static List<FrameworkMethod> computeParameterFromIterator(Method method, Iterator iterator) {
        List<FrameworkMethod> methodWithParameters = new ArrayList<>();
        for (; iterator.hasNext(); ) {
            Object caseData = iterator.next();
            if (caseData instanceof Object[]) {
                methodWithParameters.add(new FrameworkMethodWithParameters(method, (Object[]) caseData));
            } else {
                methodWithParameters.add(new FrameworkMethodWithParameters(method, new Object[]{caseData}));
            }
        }
        return methodWithParameters;
    }

    private static List<FrameworkMethod> computeParameterFromArray(Method method, Object[][] array) {
        List<FrameworkMethod> methodWithParameters = new ArrayList<>();
        for (Object[] caseData : array) {
            methodWithParameters.add(new FrameworkMethodWithParameters(method, (Object[]) caseData));
        }
        return methodWithParameters;
    }
}
