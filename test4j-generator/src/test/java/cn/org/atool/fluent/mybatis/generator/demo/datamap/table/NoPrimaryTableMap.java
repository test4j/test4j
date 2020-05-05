package cn.org.atool.fluent.mybatis.generator.demo.datamap.table;

import cn.org.atool.fluent.mybatis.annotation.ColumnDef;
import cn.org.atool.fluent.mybatis.annotation.ColumnDef.PrimaryType;
import com.baomidou.mybatisplus.annotation.TableName;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

import java.util.Date;
import java.util.function.Consumer;

import cn.org.atool.fluent.mybatis.generator.demo.mapping.NoPrimaryMP;
import cn.org.atool.fluent.mybatis.generator.demo.mapping.NoPrimaryMP.Column;

/**
 * @ClassName NoPrimaryTableMap
 * @Description NoPrimaryTableMap
 *
 * @author ${author}
 */
@TableName(NoPrimaryMP.Table_Name)
public class NoPrimaryTableMap extends DataMap<NoPrimaryTableMap> {
    /**
     * 设置no_primary对象column_1字段值
     */
    @ColumnDef(type = "int(11)")
    public transient final KeyValue<NoPrimaryTableMap> column1 = new KeyValue(this, Column.column_1);
    /**
     * 设置no_primary对象column_2字段值
     */
    @ColumnDef(type = "varchar(100)")
    public transient final KeyValue<NoPrimaryTableMap> column2 = new KeyValue(this, Column.column_2);

    public NoPrimaryTableMap() {
        super();
    }

    public NoPrimaryTableMap(int size) {
        super(size);
    }

    /**
     * 创建NoPrimaryTableMap
     * 并初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
     *
     */
    public NoPrimaryTableMap init() {
        return this;
    }

    public NoPrimaryTableMap with(Consumer<NoPrimaryTableMap> init) {
        init.accept(this);
        return this;
    }

    public static NoPrimaryTableMap create() {
        return new NoPrimaryTableMap(1);
    }

    public static NoPrimaryTableMap create(int size) {
        return new NoPrimaryTableMap(size);
    }

    public static class Factory {
        public NoPrimaryTableMap create() {
            return NoPrimaryTableMap.create();
        }

        public NoPrimaryTableMap create(int size) {
            return NoPrimaryTableMap.create(size);
        }

        public NoPrimaryTableMap createWithInit() {
            return NoPrimaryTableMap.create(1).init();
        }

        public NoPrimaryTableMap createWithInit(int size) {
            return NoPrimaryTableMap.create(size).init();
        }
    }
}