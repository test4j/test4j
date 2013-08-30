package org.jtester.module.inject;

import static org.jtester.tools.commons.AnnotationHelper.getFieldsAnnotatedWith;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jtester.module.core.Module;
import org.jtester.module.core.TestListener;
import org.jtester.module.inject.annotations.Inject;
import org.jtester.module.inject.annotations.TestedObject;
import org.jtester.module.inject.utility.InjectionModuleHelper;
import org.jtester.tools.commons.AnnotationHelper;
import org.jtester.tools.commons.ClazzHelper;
import org.jtester.tools.commons.StringHelper;
import org.jtester.tools.reflector.FieldAccessor;
import org.jtester.tools.reflector.PropertyAccessor;
import org.jtester.tools.reflector.imposteriser.JTesterProxy;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class InjectModule implements Module {
    public void init() {
    }

    public void afterInit() {
    }

    /**
     * jtester扩展的注入<br>
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
        Set<Field> targets = AnnotationHelper.getFieldsAnnotatedWith(testedClazz, TestedObject.class);
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
                Object injectedObject = JTesterProxy.proxy(testedObject.getClass(), injectField);
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
     * @param inject
     * @return
     */
    Map<String, Object> findValuesForInejctInto(Class testedClazz, Object testedObject) {
        Set<Field> fields = AnnotationHelper.getFieldsAnnotatedWith(testedClazz, Inject.class);
        Map<String, Object> values = new HashMap<String, Object>();
        for (Field field : fields) {
            Inject inject = field.getAnnotation(Inject.class);
            if (inject.targets().length == 0) {
                try {
                    Object value = JTesterProxy.proxy(testedClazz, field);
                    values.put(field.getName(), value);
                } catch (IllegalArgumentException ie) {
                    Object value = new FieldAccessor(testedClazz, field).get(testedObject);
                    values.put(field.getName(), value);
                }
            }
        }
        return values;
    }

    /**
     * 把对象injectedObject注入到testedObject对应的变量targets的属性中
     * 
     * @param testedObject 测试类实例
     * @param injectedObject 被注入实例
     * @param injectedClazz 被注入实例定义类型
     * @param targets 要注入的对象列表
     * @param properties 注入到对象的那个属性中列表；如果属性为空，则按类型注入
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

    public TestListener getTestListener() {
        return new InjectTestListener();
    }

    protected class InjectTestListener extends TestListener {
        @Override
        public void beforeRunning(Object testObject, Method testMethod) {
            injectInto(testObject);
        }

        @Override
        protected String getName() {
            return "InjectTestListener";
        }
    }
}
