package org.test4j.hamcrest.iassert.interal;

import org.hamcrest.Matcher;
import org.test4j.hamcrest.matcher.modes.ItemsMode;
import org.test4j.hamcrest.matcher.property.PropertyAllItemsMatcher;
import org.test4j.hamcrest.matcher.property.PropertyAnyItemMatcher;
import org.test4j.hamcrest.matcher.property.ReflectionEqualMatcher;
import org.test4j.hamcrest.matcher.property.reflection.EqMode;


/**
 * @param <T>
 * @param <E>
 * @author darui.wudr
 */
@SuppressWarnings("rawtypes")
public interface IListAssert<T, E extends IAssert> extends IAssert<T, E> {
    /**
     * want: the actual value should be equals to the expected value specified
     * by argument.
     *
     * @param expected
     * @param modes
     * @return
     */
    default E isEqualTo(Object expected, EqMode... modes) {
        ReflectionEqualMatcher matcher = new ReflectionEqualMatcher(expected, modes);
        return this.assertThat(matcher);
    }

    /**
     * the property value of all/any(specified by ItemsMode) items in
     * collection(array) should be matched by the matcher.
     *
     * @param itemsMode All or Any Items in Collection or Array.
     * @param property  the property name of items in collection or array.
     * @param matcher   Hamcrest Matcher
     * @return
     */
    default E propertyMatch(ItemsMode itemsMode, String property, Matcher matcher) {
        switch (itemsMode) {
            case AllItems:
                PropertyAllItemsMatcher m1 = new PropertyAllItemsMatcher(property, matcher);
                return this.assertThat(m1);
            case AnyItems:
                PropertyAnyItemMatcher m2 = new PropertyAnyItemMatcher(property, matcher);
                return this.assertThat(m2);
            default:
                throw new RuntimeException("the argument[ItemsMode] of property match API can't be null.");
        }
    }
}
