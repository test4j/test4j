package org.test4j.module.database.sql;

import org.test4j.asserts.iassert.impl.ArrayAssert;
import org.test4j.asserts.iassert.impl.CollectionAssert;
import org.test4j.asserts.iassert.impl.SqlAssert;
import org.test4j.asserts.iassert.intf.IArrayAssert;
import org.test4j.asserts.iassert.intf.ICollectionAssert;
import org.test4j.asserts.iassert.intf.ISqlAssert;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SqlList extends ArrayList<SqlContext> {
    /**
     * 返回第一条sql语句
     *
     * @return
     */
    public String firstSql() {
        return this.sql(0);
    }

    public String sql(int index) {
        return this.isEmpty() || this.size() < index ? null : this.get(index).getSql();
    }

    /**
     * 返回sql列表
     *
     * @return
     */
    public List<String> sqls() {
        return this.stream()
                .map(SqlContext::getSql)
                .collect(toList());
    }

    /**
     * 返回所以sql参数列表
     *
     * @return
     */
    public Object[][] parameters() {
        List<Object> list = this.stream()
                .map(SqlContext::getParameters)
                .collect(toList());
        return (Object[][]) list.toArray();
    }

    public Object[] parameter(int index) {
        return this.isEmpty() || this.size() < index ? null : this.get(index).getParameters();
    }

    /**
     * 返回第一条sql语句参数
     *
     * @return
     */
    public Object[] firstParameter() {
        return this.parameter(0);
    }

    public ICollectionAssert wantAllSql() {
        return new CollectionAssert(sqls());
    }

    public IArrayAssert wantAllParameters() {
        return new ArrayAssert(parameters());
    }

    public ISqlAssert wantFirstSql() {
        return new SqlAssert(firstSql());
    }

    public IArrayAssert wantFirstPara() {
        return new ArrayAssert(firstParameter());
    }

    public ISqlAssert wantSql(int index) {
        return new SqlAssert(this.sql(index));
    }

    public IArrayAssert wantPara(int index) {
        return new ArrayAssert(this.parameter(index));
    }
}
