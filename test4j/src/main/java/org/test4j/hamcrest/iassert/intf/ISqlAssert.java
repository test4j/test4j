package org.test4j.hamcrest.iassert.intf;

import org.test4j.hamcrest.iassert.impl.ArrayAssert;
import org.test4j.hamcrest.iassert.impl.StringAssert;
import org.test4j.module.database.utility.SQLUtility;

public interface ISqlAssert extends IStringAssert {
    /**
     * 断言sql语句表=table
     *
     * @param table
     * @return
     */
    default ISqlAssert from(String table) {
        String value = SQLUtility.parseTable(this.getAssertObject().valueToString());
        new StringAssert(value).isEqualTo(table);
        return this;
    }

    /**
     * 断言where条件部分
     *
     * @return
     */
    default IStringAssert where() {
        String where = SQLUtility.parseWhere(this.getAssertObject().valueToString());
        return new StringAssert(where);
    }

    /**
     * 断言select字段列表
     *
     * @return
     */
    default IArrayAssert select() {
        String[] fields = SQLUtility.parseSelect(this.getAssertObject().valueToString());
        return new ArrayAssert(fields);
    }
}
