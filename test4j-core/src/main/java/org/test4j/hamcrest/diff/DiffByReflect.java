package org.test4j.hamcrest.diff;

import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.tools.commons.Reflector;
import org.test4j.tools.reflector.MethodAccessor;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * 反射比较
 *
 * @author darui.wu
 * @create 2020/4/13 1:42 下午
 */
public class DiffByReflect extends BaseDiff {

    /**
     * 跳过java内部类的反射比较
     */
    static final String JAVA_INTERNAL_TYPE = "java.";


    public DiffByReflect(BaseDiff diff) {
        super(diff);
    }

    public DiffByReflect(EqMode... modes) {
        super(modes);
    }

    @Override
    public DiffMap compare(Object parentKey, Object actual, Object expect) {
        if (validateNull(parentKey, actual, expect)) {
            return this.diffMap;
        }
        if (this.isDate(actual)) {
            if (!ignoreDateType) {
                this.compareDate(parentKey, actual, expect);
            }
            return this.diffMap;
        }
        if (actual instanceof Number) {
            this.compareNumber(parentKey, actual, expect);
        } else if (actual.getClass().getName().startsWith(JAVA_INTERNAL_TYPE)) {
            // 跳过java内部类的反射比较
            Object _expect = super.asObject(expect, asString);
            Object _actual = super.asObject(actual, asString);
            if (!_expect.equals(_actual)) {
                this.diffMap.add(parentKey, actual, expect);
            }
        } else if (expect instanceof String) {
            if (asString) {
                if (!asObject(actual, asString).equals(expect)) {
                    this.diffMap.add(parentKey, actual, expect);
                }
            } else {
                this.diffMap.add(parentKey, actual, expect);
            }
        } else {
            Set<Method> list = Reflector.getAllGetterMethod(expect.getClass());
            for (Method method : list) {
                Object expectItem = MethodAccessor.method(method).invoke(expect);
                if (expectItem == null && this.ignoreNull) {
                    this.diffMap.addIgnore();
                    continue;
                }
                String methodName = method.getName();
                Object actualItem = MethodAccessor.invoke(actual, methodName);
                new DiffFactory(this).compare(parentKey + "." + this.propertyName(methodName), actualItem, expectItem);
            }
        }
        return this.diffMap;
    }

    /**
     * 数字比较
     *
     * @param parentKey
     * @param actual
     * @param expect
     */
    private void compareNumber(Object parentKey, Object actual, Object expect) {
        if (!asString && expect instanceof String) {
            this.diffMap.add(parentKey, actual, expect);
        } else {
            BigDecimal _actual = new BigDecimal(String.valueOf(actual));
            BigDecimal _expect = new BigDecimal(String.valueOf(expect));
            if (!_actual.equals(_expect)) {
                this.diffMap.add(parentKey, actual, expect);
            }
        }
    }

    private String propertyName(String methodName) {
        if (methodName.startsWith("is")) {
            return methodName.substring(2, 3).toLowerCase() + methodName.substring(3);
        } else {
            return methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
        }
    }

    /**
     * 是否时间类型
     *
     * @param actual
     * @return
     */
    private boolean isDate(Object actual) {
        if (actual instanceof Date) {
            return true;
        } else if (actual instanceof Calendar) {
            return true;
        } else {
            return actual instanceof Temporal;
        }
    }

    /**
     * 日期比较
     *
     * @param parentKey
     * @param actual
     * @param expect
     */
    private void compareDate(Object parentKey, Object actual, Object expect) {
        if (asString || expect instanceof String) {
            String _expect = (String) asObject(expect, true);
            String _actual = (String) asObject(actual, true);
            if (!_actual.contains(_expect)) {
                this.diffMap.add(parentKey, actual, _expect);
            }
        } else if (!actual.equals(expect)) {
            this.diffMap.add(parentKey, actual, expect);
        }
    }
}