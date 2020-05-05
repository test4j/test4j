package cn.org.atool.fluent.mybatis.generator.demo.datamap.entity;

import cn.org.atool.fluent.mybatis.generator.demo.mapping.TUserMP.Property;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * @ClassName TUserEntityMap
 * @Description TUserEntityMap
 *
 * @author ${author}
 */
public class TUserEntityMap extends DataMap<TUserEntityMap> {
    /**
     * 设置TUserEntity对象id字段值
     */
    public transient final KeyValue<TUserEntityMap> id = new KeyValue(this, Property.id);
    /**
     * 设置TUserEntity对象gmtCreated字段值
     */
    public transient final KeyValue<TUserEntityMap> gmtCreated = new KeyValue(this, Property.gmtCreated);
    /**
     * 设置TUserEntity对象gmtModified字段值
     */
    public transient final KeyValue<TUserEntityMap> gmtModified = new KeyValue(this, Property.gmtModified);
    /**
     * 设置TUserEntity对象isDeleted字段值
     */
    public transient final KeyValue<TUserEntityMap> isDeleted = new KeyValue(this, Property.isDeleted);
    /**
     * 设置TUserEntity对象addressId字段值
     */
    public transient final KeyValue<TUserEntityMap> addressId = new KeyValue(this, Property.addressId);
    /**
     * 设置TUserEntity对象age字段值
     */
    public transient final KeyValue<TUserEntityMap> age = new KeyValue(this, Property.age);
    /**
     * 设置TUserEntity对象userName字段值
     */
    public transient final KeyValue<TUserEntityMap> userName = new KeyValue(this, Property.userName);
    /**
     * 设置TUserEntity对象version字段值
     */
    public transient final KeyValue<TUserEntityMap> version = new KeyValue(this, Property.version);

    public TUserEntityMap() {
        super();
    }

    public TUserEntityMap(int size) {
        super(size);
    }

    public static TUserEntityMap create() {
        return new TUserEntityMap();
    }

    public static TUserEntityMap create(int size) {
        return new TUserEntityMap(size);
    }

    public static class Factory {
        public TUserEntityMap create() {
            return TUserEntityMap.create();
        }

        public TUserEntityMap create(int size) {
            return TUserEntityMap.create(size);
        }
    }
}
