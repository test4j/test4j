package org.test4j.hamcrest.matcher.property;

import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.test4j.tools.commons.ArrayHelper;
import org.test4j.tools.commons.ListHelper;
import org.test4j.tools.reflector.PropertyAccessor;


/**
 * 属性值集合或属性值作为一个对象，要满足指定的断言
 *
 * @author darui.wudr
 */
@SuppressWarnings("rawtypes")
public class PropertyAllItemsMatcher extends BaseMatcher {
    private String property;
    private Matcher matcher;

    public PropertyAllItemsMatcher(String property, Matcher matcher) {
        this.property = property;
        this.matcher = matcher;
        if (this.property == null) {
            throw new RuntimeException("the properties can't be null.");
        }
    }

    public boolean matches(Object actual) {
        if (ArrayHelper.isCollOrArray(actual) == false) {
            buff.append("PropertyItemsMatcher, the actual value must be a array or collection.");
            return false;
        }
        List list = ListHelper.toList(actual);
        actualItems = PropertyAccessor.getPropertyOfList(list, property, true);
        for (Object item : actualItems) {
            boolean match = this.matcher.matches(item);
            if (match == false) {
                return false;
            }
        }
        return true;
    }

    private final StringBuilder buff = new StringBuilder();
    private List actualItems;

    public void describeTo(Description description) {
        description.appendText("the propery" + this.property + " values of object is:\n");
        description.appendText(ListHelper.toString(actualItems)).appendText("\n");

        description.appendDescriptionOf(this.matcher);
    }
}
