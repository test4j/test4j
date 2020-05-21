package cn.org.atool.fluent.mybatis.generator.demo.query;

import cn.org.atool.fluent.mybatis.and.*;
import cn.org.atool.fluent.mybatis.condition.AbstractWrapper;
import cn.org.atool.fluent.mybatis.condition.BaseQueryAnd;
import cn.org.atool.fluent.mybatis.condition.BaseUpdateSet;
import cn.org.atool.fluent.mybatis.condition.BaseWrapperOrder;
import cn.org.atool.fluent.mybatis.condition.interfaces.IProperty2Column;
import cn.org.atool.fluent.mybatis.generator.demo.mapping.NoAutoIdMP.Property;
import cn.org.atool.fluent.mybatis.generator.demo.mapping.NoAutoIdMP.Column;


/**
 * <p>
 * NoAutoIdEntityWrapperHelper
 * NoAutoIdEntity 查询更新帮助类
 * </p>
 *
 * @author generate code
 */
class NoAutoIdEntityWrapperHelper {
    public static class And<Q extends AbstractWrapper & IProperty2Column> extends BaseQueryAnd<Q> {
        public final AndString<Q> id;
        public final AndString<Q> column1;

        And(Q query) {
            super(query);
            this.id = new AndString<>(query, Column.id, Property.id);
            this.column1 = new AndString<>(query, Column.column_1, Property.column1);
        }
    }

    public static abstract class BaseOrder<Q extends AbstractWrapper & IProperty2Column, O extends BaseOrder>
            extends BaseWrapperOrder<Q> {
        public final ColumnOrder<Q, O> id;
        public final ColumnOrder<Q, O> column1;

        public BaseOrder(Q query) {
            super(query);
            this.id = new ColumnOrder(query, Column.id, this);
            this.column1 = new ColumnOrder(query, Column.column_1, this);
        }
    }

    public static class QueryOrder extends BaseOrder<NoAutoIdEntityQuery, QueryOrder> {

        public QueryOrder(NoAutoIdEntityQuery query) {
            super(query);
        }
    }

    public static class UpdateOrder extends BaseOrder<NoAutoIdEntityUpdate, UpdateOrder> {

        public UpdateOrder(NoAutoIdEntityUpdate update) {
            super(update);
        }
    }

    public static class Set extends BaseUpdateSet<NoAutoIdEntityUpdate> {
        public final SetString<NoAutoIdEntityUpdate> id;
        public final SetString<NoAutoIdEntityUpdate> column1;

        public Set(NoAutoIdEntityUpdate update) {
            super(update);
            this.id = new SetString<>(update, Column.id, Property.id);
            this.column1 = new SetString<>(update, Column.column_1, Property.column1);
        }
    }
}