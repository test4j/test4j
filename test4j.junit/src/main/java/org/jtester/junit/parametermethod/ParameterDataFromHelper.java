package org.jtester.junit.parametermethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jtester.junit.annotations.DataFrom;
import org.jtester.tools.commons.MethodHelper;
import org.jtester.tools.commons.Reflector;
import org.jtester.tools.exception.NewInstanceException;
import org.jtester.tools.exception.NoSuchMethodRuntimeException;

@SuppressWarnings("rawtypes")
public class ParameterDataFromHelper {
    /**
     * 构造一系列有参的测试方法
     * 
     * @param testClazz 当前测试类class
     * @param testMethod 测试方法
     * @param dataFrom
     * @return
     */
    public static List<FrameworkMethodWithParameters> computeParameterizedTestMethods(Class testClazz,
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

    private static List<FrameworkMethodWithParameters> computeParameterziedFromDataProviderMethod(Class testClazz,
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

    @SuppressWarnings("unchecked")
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
                throw new RuntimeException(
                        "The @DataFrom method isn't a static method or isn't declared in a concrete class.", e2);
            }
        }
    }

    private static List<FrameworkMethodWithParameters> computeParameterFromIterator(Method method, Iterator iterator) {
        List<FrameworkMethodWithParameters> methodWithParameters = new ArrayList<FrameworkMethodWithParameters>();
        for (; iterator.hasNext();) {
            Object caseData = iterator.next();
            if (caseData instanceof Object[]) {
                methodWithParameters.add(new FrameworkMethodWithParameters(method, (Object[]) caseData));
            } else {
                methodWithParameters.add(new FrameworkMethodWithParameters(method, new Object[] { caseData }));
            }
        }
        return methodWithParameters;
    }

    private static List<FrameworkMethodWithParameters> computeParameterFromArray(Method method, Object[][] array) {
        List<FrameworkMethodWithParameters> methodWithParameters = new ArrayList<FrameworkMethodWithParameters>();
        for (Object[] caseData : array) {
            methodWithParameters.add(new FrameworkMethodWithParameters(method, (Object[]) caseData));
        }
        return methodWithParameters;
    }
}
