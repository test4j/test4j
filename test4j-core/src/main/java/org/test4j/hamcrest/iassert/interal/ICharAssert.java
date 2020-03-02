package org.test4j.hamcrest.iassert.interal;

import org.hamcrest.Matcher;
import org.hamcrest.core.AnyOf;
import org.hamcrest.core.IsNot;
import org.test4j.hamcrest.matcher.string.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface ICharAssert<T, E extends ICharAssert> extends IAssert<T, E> {
    /**
     * 期望2个字符串在modes模式下是相等的<br>
     * same as "isEqualTo(...)"
     *
     * @param expected
     * @param modes
     * @return
     */
    default E eq(String expected, StringMode... modes) {
        return this.isEqualTo(expected, modes);
    }

    /**
     * 期望2个字符串在modes模式下是相等的<br>
     * same as "eq(...)"
     *
     * @param expected
     * @param modes
     * @return
     */
    default E isEqualTo(String expected, StringMode... modes) {
        StringMatcher matcher = new StringEqualMatcher(expected);
        matcher.setStringModes(modes);
        return (E) this.assertThat(matcher);
    }

    /**
     * 断言字符串包含期望的字串${expected}
     *
     * @param expected 期望的字串
     * @param modes    字符串预处理模式
     * @return
     */
    default E contains(String expected, StringMode... modes) {
        StringContainMatcher matcher = new StringContainMatcher(new String[]{expected}, modes);
        return (E) this.assertThat(matcher);
    }

    /**
     * 断言字符串包含指定的若干个子字符串
     *
     * @param expecteds 期望的字符串组
     * @param modes     字符串处理模式
     * @return
     */
    default E contains(String[] expecteds, StringMode... modes) {
        StringContainMatcher matcher = new StringContainMatcher(expecteds, modes);
        return (E) this.assertThat(matcher);
    }

    /**
     * 断言字符串以${expected}子串结尾
     *
     * @param expected 期望的字串
     * @param modes    字符串预处理模式
     * @return
     */
    default E end(String expected, StringMode... modes) {
        StringEndWithMatcher matcher = new StringEndWithMatcher(expected);
        matcher.setStringModes(modes);
        return (E) this.assertThat(matcher);
    }

    /**
     * 断言字符串以${expected}子串开头
     *
     * @param expected 期望的字串
     * @param modes    字符串预处理模式
     * @return
     */
    default E start(String expected, StringMode... modes) {
        StringStartWithMatcher matcher = new StringStartWithMatcher(expected);
        matcher.setStringModes(modes);
        return (E) this.assertThat(matcher);
    }


    /**
     * 断言字符串中依次包含字串列表expecteds
     *
     * @param expecteds
     * @return
     */
    default E containsInOrder(String... expecteds) {
        Iterable<String> substrings = Arrays.asList(expecteds);
        StringContainsInOrder matcher = new StringContainsInOrder(substrings);
        return (E) this.assertThat(matcher);
    }

    /**
     * 断言字符串中依次包含若干特定模式的子字符串
     *
     * @param expecteds
     * @param modes
     * @return
     */
    default E containsInOrder(String[] expecteds, StringMode... modes) {
        Iterable<String> substrings = Arrays.asList(expecteds);
        StringContainsInOrder matcher = new StringContainsInOrder(substrings, modes);
        return (E) this.assertThat(matcher);
    }

    /**
     * 断言字符串不包含指定的子字符串
     *
     * @param sub
     * @param modes 字符串预处理模式
     * @return
     */
    default E notContain(String sub, StringMode... modes) {
        StringContainMatcher matcher = new StringContainMatcher(new String[]{sub}, modes);
        Matcher _matcher = IsNot.not(matcher);
        return this.assertThat(_matcher);
    }

    /**
     * 断言字符串不包含指定的子字符串
     *
     * @param subs
     * @param modes
     * @return
     */
    default E notContain(String[] subs, StringMode... modes) {
        List<Matcher<? super String>> matchers = new ArrayList<>();
        for (String sub : subs) {
            StringContainMatcher matcher = new StringContainMatcher(new String[]{sub}, modes);
            matchers.add(matcher);
        }

        Matcher _matcher = IsNot.not(AnyOf.anyOf(matchers));
        return this.assertThat(_matcher);
    }

    /**
     * 断言字符串在忽略大小写的情况下等于期望值
     *
     * @param string 期望值
     * @return
     */
    default E eqIgnoreCase(String string) {
        StringMatcher matcher = new StringEqualMatcher(string);
        matcher.setStringModes(StringMode.IgnoreCase);
        return (E) this.assertThat(matcher);
    }

    /**
     * 忽略字符串中所有的空白符情况下相等
     *
     * @param string
     * @return
     */
    default E eqIgnoreSpace(String string) {
        StringMatcher matcher = new StringEqualMatcher(string);
        matcher.setStringModes(StringMode.IgnoreSpace);
        return this.assertThat("expect equal when ignore all space.", matcher);
    }

    /**
     * 字符串中忽略连续的空白串情况下相等<br>
     * 即空白符或连续的空白符当作一个空格符处理
     *
     * @param string 期望值
     * @return
     */
    default E eqWithStripSpace(String string) {
        StringMatcher matcher = new StringEqualMatcher(string);
        matcher.setStringModes(StringMode.SameAsSpace);
        return (E) this.assertThat(matcher);
    }

    /**
     * 断言字符串不是空白串
     *
     * @return
     */
    default E notBlank(){
        Matcher matcher = new StringNotBlankMatcher();
        return this.assertThat("expect string is blank", matcher);
    }
}
