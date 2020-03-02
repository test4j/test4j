package org.test4j.module.inject;

import org.test4j.module.core.Module;
import org.test4j.module.core.internal.TestListener;
import org.test4j.tools.commons.AnnotationHelper;
import org.test4j.tools.commons.ClazzHelper;
import org.test4j.tools.commons.StringHelper;
import org.test4j.tools.reflector.FieldAccessor;
import org.test4j.tools.reflector.PropertyAccessor;
import org.test4j.tools.reflector.imposteriser.Test4JProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static org.test4j.tools.commons.AnnotationHelper.getFieldsAnnotatedWith;

@SuppressWarnings({"rawtypes", "unchecked"})
public class InjectModule implements Module {
    /**
     * 把对象injectedObject注入到testedObject对应的变量targets的属性中
     *
     * @param testedObject   测试类实例
     * @param injectedObject 被注入实例
     * @param injectedClazz  被注入实例定义类型
     * @param targets        要注入的对象列表
     * @param properties     注入到对象的那个属性中列表；如果属性为空，则按类型注入
     */
    private static void injectedInto(Object testedObject, Object injectedObject, Class injectedClazz, String[] targets,
                                     String[] properties) {
        for (int index = 0; index < targets.length; index++) {
            String target = targets[index];
            Object targetObject = PropertyAccessor.getPropertyByOgnl(testedObject, target, true);
            targetObject = ClazzHelper.getProxiedObject(targetObject);
            if (targetObject == null) {
                throw new RuntimeException("can't inject a mock object into a null object, ongl = " + target);
            }
            String property = index < properties.length ? properties[index] : null;
            if (StringHelper.isBlankOrNull(property)) {
                InjectionModuleHelper.injectIntoByType(injectedObject, injectedClazz == null ? targetObject.getClass()
                        : injectedClazz, targetObject);
            } else {
                InjectionModuleHelper.injectInto(injectedObject, targetObject, property);
            }
        }
    }

    @Override
    public void init() {
    }

    @Override
    public void afterInit() {
    }

    /**
     * test4j扩展的注入<br>
     *
     * @param testedObject
     */
    private void injectInto(Object testedObject) {
        Class testedClazz = testedObject.getClass();
        injectIntoByInejctTargets(testedObject);
        injectIntoByTestedObject(testedClazz, testedObject);
    }

    /**
     * 根据@TestedObject标识来注入对象
     *
     * @param testedObject
     */
    private void injectIntoByTestedObject(Class testedClazz, Object testedObject) {
        Set<Field> targets = AnnotationHelper.getFieldsAnnotatedWith(testedClazz, Injected.class);
        Map<String, Object> propertyValues = this.findValuesForInejctInto(testedClazz, testedObject);
        for (Field target : targets) {
            Object targetObject = new FieldAccessor(testedClazz, target).get(testedObject);
            targetObject = ClazzHelper.getProxiedObject(targetObject);
            for (Entry<String, Object> entry : propertyValues.entrySet()) {
                FieldAccessor accessor = new FieldAccessor(target.getType(), entry.getKey());
                Object value = entry.getValue();
                accessor.set(targetObject, value);
            }
        }
    }

    /**
     * 在@Inject属性中显式指定了插入目标对象
     *
     * @param testedObject
     */
    private void injectIntoByInejctTargets(Object testedObject) {
        Set<Field> injects = getFieldsAnnotatedWith(testedObject.getClass(), Inject.class);
        for (Field injectField : injects) {
            Class injectedClazz = injectField.getType();
            Inject inject = injectField.getAnnotation(Inject.class);
            if (inject.targets().length == 0) {
                continue;
            }
            try {
                Object injectedObject = Test4JProxy.proxy(testedObject.getClass(), injectField);
                injectedInto(testedObject, injectedObject, injectedClazz, inject.targets(), inject.properties());
            } catch (IllegalArgumentException e) {
                Object injectedObject = new FieldAccessor(testedObject.getClass(), injectField).get(testedObject);
                injectedInto(testedObject, injectedObject, injectedClazz, inject.targets(), inject.properties());
            }
        }
    }

    /**
     * 查找注入@TestedObject的属性对象
     *
     * @param testedClazz
     * @param testedObject
     * @return
     */
    Map<String, Object> findValuesForInejctInto(Class testedClazz, Object testedObject) {
        Set<Field> fields = AnnotationHelper.getFieldsAnnotatedWith(testedClazz, Inject.class);
        Map<String, Object> values = new HashMap<String, Object>();
        for (Field field : fields) {
            Inject inject = field.getAnnotation(Inject.class);
            if (inject.targets().length == 0) {
                try {
                    Object value = Test4JProxy.proxy(testedClazz, field);
                    values.put(field.getName(), value);
                } catch (IllegalArgumentException ie) {
                    Object value = new FieldAccessor(testedClazz, field).get(testedObject);
                    values.put(field.getName(), value);
                }
            }
        }
        return values;
    }


    @Override
    public TestListener getTestListener() {
        return new InjectTestListener();
    }

    protected class InjectTestListener extends TestListener {
        @Override
        public void beforeMethod(Object testObject, Method testMethod) {
            injectInto(testObject);
        }

        @Override
        protected String getName() {
            return "InjectTestListener";
        }
    }
}
