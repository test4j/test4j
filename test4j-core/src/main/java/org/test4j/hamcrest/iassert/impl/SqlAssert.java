package org.test4j.hamcrest.iassert.impl;

import org.test4j.hamcrest.iassert.intf.ISqlAssert;

public class SqlAssert extends StringAssert<ISqlAssert> implements ISqlAssert {
    public SqlAssert() {
        super(ISqlAssert.class);
    }

    public SqlAssert(boolean toString) {
        super(ISqlAssert.class);
        if (toString) {
            this.getAssertObject().setValue("toString");
        }
    }

    public SqlAssert(String sql) {
        super(sql, ISqlAssert.class);
    }
}
