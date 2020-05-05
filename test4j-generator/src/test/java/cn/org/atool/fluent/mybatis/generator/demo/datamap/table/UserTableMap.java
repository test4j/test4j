package cn.org.atool.fluent.mybatis.generator.demo.datamap.table;

import cn.org.atool.fluent.mybatis.annotation.ColumnDef;
import cn.org.atool.fluent.mybatis.annotation.ColumnDef.PrimaryType;
import com.baomidou.mybatisplus.annotation.TableName;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

import java.util.Date;
import java.util.function.Consumer;

import cn.org.atool.fluent.mybatis.generator.demo.mapping.UserMP;
import cn.org.atool.fluent.mybatis.generator.demo.mapping.UserMP.Column;

/**
 * @ClassName UserTableMap
 * @Description UserTableMap
 *
 * @author generate code
 */
@TableName(UserMP.Table_Name)
public class UserTableMap extends DataMap<UserTableMap> {
    /**
     * 设置t_user对象id字段值
     */
    @ColumnDef(type = "bigint(21) unsigned", primary = PrimaryType.AutoIncrease)
    public transient final KeyValue<UserTableMap> id = new KeyValue(this, Column.id);
    /**
     * 设置t_user对象gmt_created字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<UserTableMap> gmtCreated = new KeyValue(this, Column.gmt_created);
    /**
     * 设置t_user对象gmt_modified字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<UserTableMap> gmtModified = new KeyValue(this, Column.gmt_modified);
    /**
     * 设置t_user对象is_deleted字段值
     */
    @ColumnDef(type = "tinyint(2)")
    public transient final KeyValue<UserTableMap> isDeleted = new KeyValue(this, Column.is_deleted);
    /**
     * 设置t_user对象address_id字段值
     */
    @ColumnDef(type = "bigint(21)")
    public transient final KeyValue<UserTableMap> addressId = new KeyValue(this, Column.address_id);
    /**
     * 设置t_user对象age字段值
     */
    @ColumnDef(type = "int(11)")
    public transient final KeyValue<UserTableMap> age = new KeyValue(this, Column.age);
    /**
     * 设置t_user对象user_name字段值
     */
    @ColumnDef(type = "varchar(45)")
    public transient final KeyValue<UserTableMap> userName = new KeyValue(this, Column.user_name);
    /**
     * 设置t_user对象version字段值
     */
    @ColumnDef(type = "varchar(45)")
    public transient final KeyValue<UserTableMap> version = new KeyValue(this, Column.version);

    public UserTableMap() {
        super();
    }

    public UserTableMap(int size) {
        super(size);
    }

    /**
     * 创建UserTableMap
     * 并初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
     *
     */
    public UserTableMap init() {
        this.id.autoIncrease();
        this.gmtCreated.values(new Date());
        this.gmtModified.values(new Date());
        this.isDeleted.values(false);
        return this;
    }

    public UserTableMap with(Consumer<UserTableMap> init) {
        init.accept(this);
        return this;
    }

    public static UserTableMap create() {
        return new UserTableMap(1);
    }

    public static UserTableMap create(int size) {
        return new UserTableMap(size);
    }

    public static class Factory {
        public UserTableMap create() {
            return UserTableMap.create();
        }

        public UserTableMap create(int size) {
            return UserTableMap.create(size);
        }

        public UserTableMap createWithInit() {
            return UserTableMap.create(1).init();
        }

        public UserTableMap createWithInit(int size) {
            return UserTableMap.create(size).init();
        }
    }
}