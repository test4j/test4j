package org.test4j;

import org.test4j.asserts.IWant;
import org.test4j.module.database.IDatabase;
import org.test4j.tools.IUtils;
import org.test4j.tools.commons.ArrayHelper;

import java.util.ArrayList;
import java.util.List;

public interface Test4J extends ISpring, IDatabase, IWant, IUtils {
    /**
     * 构造空list
     *
     * @return
     */
    default List arr() {
        return new ArrayList();
    }

    /***
     * 构造list对象
     *
     * @param value
     * @param values
     * @return
     */
    default List arr(Object value, Object... values) {
        return ArrayHelper.arr(value, values);
    }
}