package cn.org.atool.fluent.mybatis.generator.demo.datamap.table;

import cn.org.atool.fluent.mybatis.annotation.ColumnDef;
import cn.org.atool.fluent.mybatis.annotation.ColumnDef.PrimaryType;
import com.baomidou.mybatisplus.annotation.TableName;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

import java.util.Date;
import java.util.function.Consumer;

import cn.org.atool.fluent.mybatis.generator.demo.mapping.TUserMP;
import cn.org.atool.fluent.mybatis.generator.demo.mapping.TUserMP.Column;

/**
 * @ClassName TUserTableMap
 * @Description TUserTableMap
 *
 * @author ${author}
 */
@TableName(TUserMP.Table_Name)
public class TUserTableMap extends DataMap<TUserTableMap> {
    /**
     * 设置t_user对象id字段值
     */
    @ColumnDef(type = "bigint(21) unsigned", primary = PrimaryType.AutoIncrease)
    public transient final KeyValue<TUserTableMap> id = new KeyValue(this, Column.id);
    /**
     * 设置t_user对象gmt_created字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<TUserTableMap> gmtCreated = new KeyValue(this, Column.gmt_created);
    /**
     * 设置t_user对象gmt_modified字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<TUserTableMap> gmtModified = new KeyValue(this, Column.gmt_modified);
    /**
     * 设置t_user对象is_deleted字段值
     */
    @ColumnDef(type = "tinyint(2)")
    public transient final KeyValue<TUserTableMap> isDeleted = new KeyValue(this, Column.is_deleted);
    /**
     * 设置t_user对象address_id字段值
     */
    @ColumnDef(type = "bigint(21)")
    public transient final KeyValue<TUserTableMap> addressId = new KeyValue(this, Column.address_id);
    /**
     * 设置t_user对象age字段值
     */
    @ColumnDef(type = "int(11)")
    public transient final KeyValue<TUserTableMap> age = new KeyValue(this, Column.age);
    /**
     * 设置t_user对象user_name字段值
     */
    @ColumnDef(type = "varchar(45)")
    public transient final KeyValue<TUserTableMap> userName = new KeyValue(this, Column.user_name);
    /**
     * 设置t_user对象version字段值
     */
    @ColumnDef(type = "varchar(45)")
    public transient final KeyValue<TUserTableMap> version = new KeyValue(this, Column.version);

    public TUserTableMap() {
        super();
    }

    public TUserTableMap(int size) {
        super(size);
    }

    /**
     * 创建TUserTableMap
     * 并初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
     *
     */
    public TUserTableMap init() {
        this.id.autoIncrease();
        this.gmtCreated.values(new Date());
        this.gmtModified.values(new Date());
        return this;
    }

    public TUserTableMap with(Consumer<TUserTableMap> init) {
        init.accept(this);
        return this;
    }

    public static TUserTableMap create() {
        return new TUserTableMap(1);
    }

    public static TUserTableMap create(int size) {
        return new TUserTableMap(size);
    }

    public static class Factory {
        public TUserTableMap create() {
            return TUserTableMap.create();
        }

        public TUserTableMap create(int size) {
            return TUserTableMap.create(size);
        }

        public TUserTableMap createWithInit() {
            return TUserTableMap.create(1).init();
        }

        public TUserTableMap createWithInit(int size) {
            return TUserTableMap.create(size).init();
        }
    }
}