package org.test4j.module.spec.internal;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.test4j.ICore;
import org.test4j.integration.spring.SpringEnv;
import org.test4j.module.spec.SpecModule;
import org.test4j.module.spec.annotations.*;
import org.test4j.tools.commons.AnnotationHelper;
import org.test4j.tools.commons.ClazzHelper;
import org.test4j.tools.reflector.FieldAccessor;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.stream.Collectors.joining;

public class MixProxy<T> implements MethodInterceptor {
    private static final Set<String> Ignore_Methods = new HashSet<String>() {
        {
            this.add("toString");
            this.add("hashCode");
            this.add("setData");
            this.add("getData");
        }
    };

    private String klass;
    private boolean injected = false;
    private Lock lock = new ReentrantLock();

    private MixProxy(Class<T> klass) {
        this.klass = klass.getName();
    }

    /**
     * 动态生成代理对象
     *
     * @param klass
     * @param <T>
     * @return
     */
    public static <T> T proxy(Class<T> klass) {
        Enhancer enhancer = new Enhancer();
        {
            enhancer.setCallback(new MixProxy(klass));
            enhancer.setSuperclass(klass);
            enhancer.setInterfaces(new Class[]{Serializable.class});
        }
        return (T) enhancer.create();
    }

    public static void mix(Object injectedObject) {
        mix(injectedObject, injectedObject.getClass());
    }

    private static void mix(Object injectedObject, Class<?> klass) {
        Set<Field> fields = AnnotationHelper.getFieldsAnnotatedWith(klass, Mix.class);
        if (fields == null) {
            return;
        }
        fields.forEach(field -> {
            Object mix = MixProxy.proxy(field.getType());
            FieldAccessor.field(field).set(injectedObject, mix);
        });
    }

    public static void createMixes(Object testedObject) {
        Set<Field> fields = AnnotationHelper.getFieldsAnnotatedWith(testedObject.getClass(), Mixes.class);
        if (fields == null) {
            return;
        }
        fields.forEach(field -> {
            Object mixes = ClazzHelper.newInstance(field.getType());
            mix(mixes, field.getType());
            FieldAccessor.field(field).set(testedObject, mixes);
        });
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        String methodName = method.getName();
        String methodKlass = method.getDeclaringClass().getName();
        if (isSkipMethod(methodName, methodKlass)) {
            return methodProxy.invokeSuper(obj, args);
        }
        if (!injected) {
            this.injectSpring(obj);
        }
        String name = klass + "#" + methodName;
        ScenarioResult scenario = SpecModule.currScenario();
        if (scenario.isFailure()) {
            scenario.getLastStep().skip(name);
            return null;
        }
        String description = this.getMethodDescription(method, args);
        Object result = null;
        try {
            result = methodProxy.invokeSuper(obj, args);
            return result;
        } finally {
            try {
                scenario.getLastStep().setDescription(name, description, args, result);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isSkipMethod(String method, String klass) {
        if (Ignore_Methods.contains(method)) {
            return true;
        } else if (ICore.class.getName().equals(klass)) {
            return true;
        } else {
            return false;
        }
    }

    private void injectSpring(Object obj) {
        lock.lock();
        try {
            if (injected == true) {
                return;
            }
            SpringEnv.injectSpringBeans(obj);
        } finally {
            injected = true;
            lock.unlock();
        }
    }

    private String getMethodDescription(Method method, Object[] args) {
        Step step = method.getDeclaredAnnotation(Step.class);
        if (step != null) {
            return step.value();
        }
        Given given = method.getDeclaredAnnotation(Given.class);
        if (given != null) {
            return given.value();
        }
        When when = method.getDeclaredAnnotation(When.class);
        if (when != null) {
            return when.value();
        }
        Then then = method.getDeclaredAnnotation(Then.class);
        if (then != null) {
            return then.value();
        }
        return Arrays.stream(args).map(arg -> "{}").collect(joining(", "));
    }
}