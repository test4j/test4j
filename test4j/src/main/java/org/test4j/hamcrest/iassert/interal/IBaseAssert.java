package org.test4j.hamcrest.iassert.interal;

import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIn;
import org.hamcrest.core.*;
import org.hamcrest.object.HasToString;
import org.test4j.hamcrest.iassert.intf.IStringAssert;
import org.test4j.hamcrest.matcher.clazz.ClassAssignFromMatcher;
import org.test4j.tools.commons.ListHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * the basic asserting matcher
 *
 * @param <T>
 * @param <E>
 * @author darui.wudr
 */
@SuppressWarnings("rawtypes")
public interface IBaseAssert<T, E extends IAssert> extends Matcher<T>, IAssert<T, E> {
    /**
     * 对象的toString等于期望值
     *
     * @param expected
     * @return
     */
    default E eqToString(String expected) {
        Matcher<String> matcher = HasToString.hasToString(expected);
        return this.assertThat(matcher);
    }

    /**
     * 对象的toString符合断言器判断
     *
     * @param matcher
     * @return
     */
    default E eqToString(IStringAssert matcher) {
        Matcher<String> _matcher = HasToString.hasToString(matcher);
        return this.assertThat(_matcher);
    }

    /**
     * 断言对象等于期望的值<br>
     * same as method "isEqualTo(T)"
     *
     * @param expected 期望值
     * @return
     */
    default E eq(T expected) {
        Matcher matcher = IsEqual.equalTo(expected);
        return this.assertThat(matcher);
    }

    /**
     * 断言对象等于期望的值<br>
     * same as method "eq(T)"
     *
     * @param expected 期望值
     * @return
     */
    default E isEqualTo(T expected) {
        Matcher matcher = IsEqual.equalTo(expected);
        return this.assertThat(matcher);
    }

    /**
     * 断言对象等于期望的值
     *
     * @param message  错误信息
     * @param expected 期望值
     * @return
     */
    default E isEqualTo(String message, T expected) {
        Object _expected = this.getAssertObject().isNumberAndConvert(expected);
        Matcher matcher = IsEqual.equalTo(_expected);
        return this.assertThat(message, matcher);
    }

    /**
     * 断言对象不等于期望的值
     *
     * @param expected 期望值
     * @return
     */
    default E notEqualTo(T expected) {
        Matcher matcher = IsNot.not(IsEqual.equalTo(expected));
        return this.assertThat(matcher);
    }

    /**
     * 断言对象可以在期望值里面找到
     *
     * @param values 期望值
     * @return
     */
    default E in(T... values) {
        Matcher<T> matcher = IsIn.isOneOf(values);
        return this.assertThat(matcher);
    }

    /**
     * 断言对象不可以在期望值里面找到
     *
     * @param values 期望值
     * @return
     */
    default E notIn(T... values) {
        Matcher _matcher = IsNot.not(IsIn.isOneOf(values));
        return this.assertThat(_matcher);
    }

    /**
     * 断言对象的类型等于期望类型
     *
     * @param expected 期望类型
     * @return
     */
    default E clazIs(Class expected) {
        Matcher matcher = Is.isA(expected);
        return this.assertThat(matcher);
    }

    /**
     * 断言对象的类型是期望类型的子类
     *
     * @param claz
     * @return
     */
    default E clazIsSubFrom(Class claz) {
        ClassAssignFromMatcher matcher = new ClassAssignFromMatcher(claz);
        return this.assertThat(matcher);
    }

    /**
     * 断言对象符合任一个对象行为定义<br>
     * same as "matchAny(...)"
     *
     * @param matcher  对象行为定义，具体定义参见 ext.test4j.hamcrest.Matcher
     * @param matchers
     * @return
     */
    default E any(E matcher, E... matchers) {
        List<Matcher<? super Object>> list = ListHelper.toList(matchers);
        list.add(matcher);
        Matcher _matcher = AnyOf.anyOf(list);
        return this.assertThat(_matcher);
    }

    /**
     * 断言对象符合所有的对象行为定义<br>
     * same as "matchAll(Matcher...)"
     *
     * @param matcher
     * @param matchers
     * @return
     */
    default E all(E matcher, E... matchers) {
        List<Matcher<? super Object>> list = ListHelper.toList(matchers);
        list.add(matcher);
        Matcher _matcher = AllOf.allOf(list);
        return this.assertThat(_matcher);
    }

    /**
     * 断言对象不符合matcher所定义的行为
     *
     * @param matcher 对象行为定义，具体定义参见 ext.test4j.hamcrest.Matcher
     * @return
     */
    default E not(E matcher) {
        Matcher<T> _matcher = IsNot.not(matcher);
        return this.assertThat(_matcher);
    }

    /**
     * 没有一个断言器可以匹配实际对象，即每一个断言器都匹配失败
     *
     * @param matcher
     * @param matchers
     * @return
     */
    default E notAny(Matcher matcher, Matcher... matchers) {
        List<Matcher<? super Object>> ms = new ArrayList<>();
        ms.add(matcher);
        for (Matcher m : matchers) {
            ms.add(m);
        }
        Matcher _matcher = IsNot.not(AnyOf.anyOf(ms));
        return this.assertThat(_matcher);
    }

    /**
     * 不是所有的断言器都可以匹配实际对象，即至少有一个断言器失败
     *
     * @param matcher
     * @param matchers
     * @return
     */
    default E notAll(Matcher matcher, Matcher... matchers) {
        List<Matcher<? super Object>> ms = new ArrayList<>();
        ms.add(matcher);
        for (Matcher m : matchers) {
            ms.add(m);
        }
        Matcher _matcher = IsNot.not(AllOf.allOf(ms));
        return this.assertThat(_matcher);
    }

    /**
     * 断言对象和期望值是同一个对象
     *
     * @param value 期望值
     * @return
     */
    default E same(T value) {
        Matcher _matcher = IsSame.sameInstance(value);
        return this.assertThat(_matcher);
    }

    /**
     * 断言对象可以使任意的值
     *
     * @return
     */
    default E any() {
        Matcher _matcher = IsAnything.anything();
        return this.assertThat(_matcher);
    }

    /**
     * 断言对象值等于null
     *
     * @return
     */
    default E isNull() {
        Matcher _matcher = IsNull.nullValue();
        return this.assertThat(_matcher);
    }

    /**
     * 断言对象值等于null
     *
     * @return
     */
    default E isNull(String message) {
        Matcher _matcher = IsNull.nullValue();
        return this.assertThat(message, _matcher);
    }

    /**
     * 断言对象值不等于null
     *
     * @return
     */
    default E notNull() {
        Matcher _matcher = IsNull.notNullValue();
        return this.assertThat(_matcher);
    }

    /**
     * 断言对象值不等于null
     *
     * @return
     */
    default E notNull(String message) {
        Matcher _matcher = IsNull.notNullValue();
        return this.assertThat(message, _matcher);
    }
}
