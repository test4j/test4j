package org.test4j.hamcrest.iassert.interal;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hamcrest.Matcher;
import org.test4j.hamcrest.matcher.LinkMatcher;
import org.test4j.json.JSON;
import org.test4j.module.core.utility.MessageHelper;

import java.util.Collection;

/**
 * test4j断言超基类接口
 *
 * @param <T>
 * @param <E>
 * @author darui.wudr
 */
@SuppressWarnings("rawtypes")
public interface IAssert<T, E extends IAssert> extends Matcher<T> {
    /**
     * 用于jmock expected参数断言的结尾<br>
     * 结束断言返回一个符合参数类型的值以满足编译
     *
     * @return 返回参数类型的默认值
     */
    T wanted();

    /**
     * 用于jmock expected参数断言的结尾<br>
     * 结束断言返回一个符合参数类型的值以满足编译
     *
     * @param <F>
     * @param claz
     * @return
     */
    <F> F wanted(Class<F> claz);

    default E print() {
        MessageHelper.info(JSON.toJSON(this.getAssertObject().getValue(), true));
        return (E) this;
    }

    E assertThat(Matcher matcher);

    E assertThat(String message, Matcher matcher);

    AssertObject getAssertObject();

    enum AssertType {
        /**
         * 立即断言模式<br>
         * want.object(o)...
         */
        AssertStyle,
        /**
         * 定义断言器方式<br>
         * the.object()...
         */
        MatcherStyle;
    }

    /**
     * 断言对象
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    class AssertObject {
        protected Class targetClass = null;

        protected Object value;

        protected AssertType type;

        protected Class<? extends IAssert> expectedClass;

        protected LinkMatcher<?> link;

        public AssertObject(AssertType type, Object value, Class targetClass) {
            this.targetClass = targetClass;
            this.value = value;
            this.type = type;
        }

        public static void assertCanComparable(Object o) {
            if (o != null && !(o instanceof Comparable)) {
                throw new AssertionError("the object[" + o + "] isn't a comparable object.");
            }
        }

        public Object isNumberAndConvert(Object expected) {
            if (expected == null) {
                return null;
            } else if (this.valueIsInteger()) {
                return Integer.parseInt(expected.toString());
            } else if (this.valueIsShort()) {
                return Short.parseShort(expected.toString());
            } else if (this.valueIsLong()) {
                return Long.parseLong(expected.toString());
            } else if (this.valueIsFloat()) {
                return Float.parseFloat(expected.toString());
            } else if (this.valueIsDouble()) {
                return Double.parseDouble(expected.toString());
            } else {
                return expected;
            }
        }

        public void assertTargetClassNotNull() {
            assert this.targetClass != null : "the value asserted must not be null." ;
        }

        public boolean valueIsDouble() {
            return this.targetClass == double.class || this.targetClass == Double.class;
        }

        public boolean valueIsFloat() {
            return this.targetClass == float.class || this.targetClass == Float.class;
        }

        public boolean valueIsLong() {
            return this.targetClass == long.class || this.targetClass == Long.class;
        }

        public boolean valueIsShort() {
            return this.targetClass == short.class || this.targetClass == Short.class;
        }

        public boolean valueIsInteger() {
            return this.targetClass == int.class || this.targetClass == Integer.class;
        }

        /**
         * 判断是否为数组断言
         *
         * @return
         */
        public boolean valueIsArray() {
            return this.targetClass != null && this.targetClass.isArray();
        }

        public boolean valueIsList() {
            return this.targetClass != null && this.targetClass.isAssignableFrom(Collection.class);
        }

        public boolean assertTypeIs(AssertType assertType) {
            return assertType != null && assertType.equals(this.type);
        }

        public String valueToString() {
            return String.valueOf(this.value);
        }

        /**
         * 断言对象时数组或者Collection
         *
         * @return
         */
        public boolean isArrayOrList() {
            return this.valueIsList() || this.valueIsArray();
        }
    }
}
