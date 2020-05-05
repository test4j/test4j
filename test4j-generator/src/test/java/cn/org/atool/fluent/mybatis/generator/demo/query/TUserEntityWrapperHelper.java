package cn.org.atool.fluent.mybatis.generator.demo.query;

import cn.org.atool.fluent.mybatis.and.*;
import cn.org.atool.fluent.mybatis.base.BaseQueryAnd;
import cn.org.atool.fluent.mybatis.base.BaseUpdateSet;
import cn.org.atool.fluent.mybatis.base.BaseWrapperOrder;
import cn.org.atool.fluent.mybatis.base.IProperty2Column;
import cn.org.atool.fluent.mybatis.generator.demo.mapping.TUserMP.Property;
import cn.org.atool.fluent.mybatis.generator.demo.mapping.TUserMP.Column;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import java.util.Date;

/**
 * <p>
 * TUserEntityWrapperHelper
 * TUserEntity 查询更新帮助类
 * </p>
 *
 * @author ${author}
 */
class TUserEntityWrapperHelper {
    public static class And<Q extends AbstractWrapper & IProperty2Column> extends BaseQueryAnd<Q> {
        public final AndObject<Long, Q> id;
        public final AndObject<Date, Q> gmtCreated;
        public final AndObject<Date, Q> gmtModified;
        public final AndObject<Integer, Q> isDeleted;
        public final AndObject<Long, Q> addressId;
        public final AndObject<Integer, Q> age;
        public final AndString<Q> userName;
        public final AndString<Q> version;

        And(Q query) {
            super(query);
            this.id = new AndObject<>(query, Column.id, Property.id);
            this.gmtCreated = new AndObject<>(query, Column.gmt_created, Property.gmtCreated);
            this.gmtModified = new AndObject<>(query, Column.gmt_modified, Property.gmtModified);
            this.isDeleted = new AndObject<>(query, Column.is_deleted, Property.isDeleted);
            this.addressId = new AndObject<>(query, Column.address_id, Property.addressId);
            this.age = new AndObject<>(query, Column.age, Property.age);
            this.userName = new AndString<>(query, Column.user_name, Property.userName);
            this.version = new AndString<>(query, Column.version, Property.version);
        }
    }

    public static abstract class BaseOrder<Q extends AbstractWrapper & IProperty2Column, O extends BaseOrder>
            extends BaseWrapperOrder<Q> {
        public final ColumnOrder<Q, O> id;
        public final ColumnOrder<Q, O> gmtCreated;
        public final ColumnOrder<Q, O> gmtModified;
        public final ColumnOrder<Q, O> isDeleted;
        public final ColumnOrder<Q, O> addressId;
        public final ColumnOrder<Q, O> age;
        public final ColumnOrder<Q, O> userName;
        public final ColumnOrder<Q, O> version;

        public BaseOrder(Q query) {
            super(query);
            this.id = new ColumnOrder(query, Column.id, this);
            this.gmtCreated = new ColumnOrder(query, Column.gmt_created, this);
            this.gmtModified = new ColumnOrder(query, Column.gmt_modified, this);
            this.isDeleted = new ColumnOrder(query, Column.is_deleted, this);
            this.addressId = new ColumnOrder(query, Column.address_id, this);
            this.age = new ColumnOrder(query, Column.age, this);
            this.userName = new ColumnOrder(query, Column.user_name, this);
            this.version = new ColumnOrder(query, Column.version, this);
        }
    }

    public static class QueryOrder extends BaseOrder<TUserEntityQuery, QueryOrder> {

        public QueryOrder(TUserEntityQuery query) {
            super(query);
        }
    }

    public static class UpdateOrder extends BaseOrder<TUserEntityUpdate, UpdateOrder> {

        public UpdateOrder(TUserEntityUpdate update) {
            super(update);
        }
    }

    public static class Set extends BaseUpdateSet<TUserEntityUpdate> {
        public final SetObject<Long, TUserEntityUpdate> id;
        public final SetObject<Date, TUserEntityUpdate> gmtCreated;
        public final SetObject<Date, TUserEntityUpdate> gmtModified;
        public final SetObject<Integer, TUserEntityUpdate> isDeleted;
        public final SetObject<Long, TUserEntityUpdate> addressId;
        public final SetObject<Integer, TUserEntityUpdate> age;
        public final SetString<TUserEntityUpdate> userName;
        public final SetString<TUserEntityUpdate> version;

        public Set(TUserEntityUpdate update) {
            super(update);
            this.id = new SetObject<>(update, Column.id, Property.id);
            this.gmtCreated = new SetObject<>(update, Column.gmt_created, Property.gmtCreated);
            this.gmtModified = new SetObject<>(update, Column.gmt_modified, Property.gmtModified);
            this.isDeleted = new SetObject<>(update, Column.is_deleted, Property.isDeleted);
            this.addressId = new SetObject<>(update, Column.address_id, Property.addressId);
            this.age = new SetObject<>(update, Column.age, Property.age);
            this.userName = new SetString<>(update, Column.user_name, Property.userName);
            this.version = new SetString<>(update, Column.version, Property.version);
        }
    }
}
