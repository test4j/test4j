package org.jtester.spec.scenario.step;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jtester.spec.ISpec;
import org.jtester.spec.Steps;
import org.jtester.spec.annotations.Named;
import org.jtester.spec.inner.IScenarioStep;
import org.jtester.spec.inner.ISpecMethod;
import org.jtester.spec.inner.StepType;
import org.jtester.tools.commons.AnnotationHelper;
import org.jtester.tools.reflector.MethodAccessor;

@SuppressWarnings("rawtypes")
public class SpecMethod implements ISpecMethod {
    private final MethodAccessor accessor;

    private final String         clazzName;

    private final String         methodName;

    private final List<String>   paraNameds;

    private final List<Type>     paraTypes;

    public SpecMethod(Method method) {
        this.accessor = new MethodAccessor(method);
        this.clazzName = method.getDeclaringClass().getName();
        this.methodName = method.getName();
        this.paraNameds = this.getParaAnnotationNames(method);
        this.paraTypes = this.getParaTypes(method);
        if (this.paraNameds.size() != this.paraTypes.size()) {
            throw new RuntimeException(
                    "the size of Parameter Named Annotation should be equal to the size of Parameter type.");
        }
    }

    @Override
    public String getClazzName() {
        return clazzName;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    List<Type> getParaTypes(Method method) {
        List<Type> types = new ArrayList<Type>();
        Type[] paras = method.getGenericParameterTypes();
        for (Type para : paras) {
            types.add(para);
        }
        return types;
    }

    List<String> getParaAnnotationNames(Method method) {
        List<String> names = new ArrayList<String>();
        Annotation[][] arrays = method.getParameterAnnotations();
        for (Annotation[] annotations : arrays) {
            String name = this.annotationName(annotations);
            if (name == null) {
                throw new RuntimeException(String.format("the argument of method[%s] missing Named annotation.",
                        method.getName()));
            } else {
                names.add(name);
            }
        }
        return names;
    }

    private String annotationName(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAssignableFrom(Named.class)) {
                return ((Named) annotation).value();
            }
        }
        return null;
    }

    @Override
    public Object execute(ISpec spec, IScenarioStep step) {
        try {
            Object[] args = step.getArguments(paraNameds, paraTypes);
            Steps instance = spec.getStepsInstance(this.getClazzName());
            Object o = this.accessor.invokeUnThrow(instance == null ? spec : instance, args);
            return o;
        } catch (Throwable e) {
            String err = String.format("Invoke spec method[%s] error:%s", step.getMethod(), e.getMessage());
            throw new RuntimeException(err, e);
        }
    }

    public static Map<SpecMethodID, ISpecMethod> findMethods(Class claz) {
        Map<SpecMethodID, ISpecMethod> map = new HashMap<SpecMethodID, ISpecMethod>();

        Set<Method> methods = findAllStepMethods(claz);
        for (Method method : methods) {
            String methodName = method.getName();
            int count = method.getParameterTypes().length;
            SpecMethodID id = new SpecMethodID(methodName, count);
            if (map.containsKey(id)) {
                String msg = String.format("the class[%s] has contain a method named %s and with %d arguments.",
                        claz.getName(), methodName, count);
                throw new RuntimeException(msg);
            } else {
                SpecMethod specMethod = new SpecMethod(method);
                map.put(id, specMethod);
            }
        }
        return map;
    }

    private static Set<Method> findAllStepMethods(Class claz) {
        Set<Method> methods = new HashSet<Method>();
        for (StepType type : StepType.values()) {
            Set<Method> found = AnnotationHelper.getMethodsAnnotatedWith(claz, type.getAnnotatonClaz());
            methods.addAll(found);
        }
        return methods;
    }

    @Override
    public String toString() {
        return clazzName + "." + methodName;
    }
}
