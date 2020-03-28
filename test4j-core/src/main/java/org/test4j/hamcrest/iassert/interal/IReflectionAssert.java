package org.test4j.hamcrest.iassert.interal;

import org.hamcrest.Matcher;
import org.test4j.hamcrest.matcher.property.*;
import org.test4j.hamcrest.matcher.property.reflection.EqMode;
import org.test4j.hamcrest.matcher.string.StringMode;
import org.test4j.tools.datagen.IDataMap;

import java.util.Map;


/**
 * 针对对象属性进行断言
 *
 * @param <E>
 * @author darui.wudr
 */

@SuppressWarnings("rawtypes")
public interface IReflectionAssert<T, E extends IAssert> extends IAssert<T, E> {
    /**
     * 断言对象指定的属性(property)值等于期望值<br>
     * same as "eqByProperty(String, Object)"
     *
     * @param property 对象属性名称
     * @param expected 期望值
     * @param modes
     * @return 断言自身
     */
    default E propertyEq(String property, Object expected, EqMode... modes) {
        if (expected instanceof Matcher) {
            throw new AssertionError("please use method[propertyMatch(String, Matcher)]");
        } else {
            PropertyEqualMatcher matcher = new PropertyEqualMatcher(expected, property, modes);
            return this.assertThat(matcher);
        }
    }

    /**
     * 断言对象指定的属性(property)值等于期望值字符串
     *
     * @param property
     * @param expected
     * @param mode
     * @param more
     * @return 断言自身
     */
    default E propertyEq(String property, String expected, StringMode mode, StringMode... more) {
        StringMode[] modes = new StringMode[more.length + 1];
        int index = 0;
        modes[index++] = mode;
        for (StringMode item : more) {
            modes[index++] = item;
        }
        PropertyEqualStringMatcher matcher = new PropertyEqualStringMatcher(expected, property, modes);
        return this.assertThat(matcher);
    }

    /**
     * 断言对象的多个属性相等<br>
     * same as "eqByProperties(String[],Object)"
     *
     * @param properties
     * @param expected
     * @param modes
     * @return 断言自身
     */
    default E propertyEq(String[] properties, Object expected, EqMode... modes) {
        if (expected instanceof Matcher) {
            throw new AssertionError("please use method[propertyMatch(String, Matcher)]");
        }
        PropertiesEqualMatcher matcher = new PropertiesEqualMatcher(expected, properties, modes);
        return this.assertThat(matcher);
    }

    /**
     * 对象的属性值符合指定的断言器要求
     *
     * @param property
     * @param matcher
     * @return 断言自身
     */
    default E propertyMatch(String property, Matcher matcher) {
        PropertyItemMatcher _matcher = new PropertyItemMatcher(property, matcher);
        return this.assertThat(_matcher);
    }

    /**
     * 通过反射比较实际值和期望值的属性时，二者的属性是相同的<br>
     * 属性如果是复杂对象，递归反射比较
     *
     * @param expected 期望对象
     * @param modes    比较模式,详见org.test4j.hamcrest.matcher.property.reflection.EqMode
     * @return 断言自身
     */
    default E eqReflect(Object expected, EqMode... modes) {
        if (expected instanceof Matcher) {
            throw new AssertionError("please use method[propertyMatch(String, Matcher)]");
        } else {
            ReflectionEqualMatcher matcher = new ReflectionEqualMatcher(expected, modes);
            return this.assertThat(matcher);
        }
    }


    /**
     * 在忽略期望值是null或默认值的情况下二者是相等的
     *
     * @param expected
     * @return 断言自身
     */
    default E eqIgnoreDefault(Object expected) {
        if (expected instanceof Matcher) {
            throw new AssertionError("please use method[propertyMatch(String, Matcher)]");
        }
        ReflectionEqualMatcher matcher = new ReflectionEqualMatcher(expected, new EqMode[]{EqMode.IGNORE_DEFAULTS});
        return this.assertThat(matcher);
    }

    /**
     * 在忽略顺序，默认值，日期的情况下二者是相等的
     *
     * @param expected
     * @return 断言自身
     */
    default E eqIgnoreAll(Object expected) {
        if (expected instanceof Matcher) {
            throw new AssertionError("please use method[propertyMatch(String, Matcher)]");
        }
        ReflectionEqualMatcher matcher = new ReflectionEqualMatcher(expected, new EqMode[]{EqMode.IGNORE_ORDER,
                EqMode.IGNORE_DEFAULTS, EqMode.IGNORE_DATES});
        return this.assertThat(matcher);
    }

    /**
     * 在忽略元素顺序情况下二者是相等的
     *
     * @param expected
     * @return 断言自身
     */
    default E eqIgnoreOrder(Object expected) {
        if (expected instanceof Matcher) {
            throw new AssertionError("please use method[propertyMatch(String, Matcher)]");
        }
        ReflectionEqualMatcher matcher = new ReflectionEqualMatcher(expected, new EqMode[]{EqMode.IGNORE_ORDER});
        return this.assertThat(matcher);
    }


    /**
     * 把实际对象按照Map中的key值取出来，进行反射比较<br>
     * 如果对象的属性不在Map中，则不进行比较<br>
     * 功能和
     * "propertyEq(String[] properties, Object expected, EqMode... modes)"类似，
     * 无非这里是把属性写到了Map中，方便一一对应
     *
     * @param expected
     * @return 断言自身
     */
    default E eqMap(Map expected, EqMode... modes) {
        if (expected instanceof IDataMap) {
            return eqDataMap((IDataMap) expected, modes);
        } else {
            return eqHashMap(expected, modes);
        }
    }

    /**
     * 断言对象和Map对象比较
     *
     * @param expected
     * @param modes
     * @return 断言自身
     */
    default E eqHashMap(Map expected, EqMode... modes) {
        MapPropertyEqaulMatcher matcher = new MapPropertyEqaulMatcher(expected, modes);
        return this.assertThat(matcher);
    }

    /**
     * 断言对象和IDataMap对象比较
     *
     * @param expected
     * @param modes
     * @return 断言自身
     */
    default E eqDataMap(IDataMap expected, EqMode... modes) {
        if (this.getAssertObject().isArrayOrList()) {
            Matcher matcher = MatcherBuilder.listEqMapMatcher(expected, modes);
            return this.assertThat(matcher);
        } else {
            MapPropertyEqaulMatcher matcher = new MapPropertyEqaulMatcher(expected.row(0), modes);
            return this.assertThat(matcher);
        }
    }
}
