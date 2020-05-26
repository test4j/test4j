package cn.org.atool.fluent.mybatis.generator.demo.datamap.entity;

import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * @ClassName UserEntityMap
 * @Description UserEntityMap
 *
 * @author generate code
 */
public class UserEntityMap extends DataMap<UserEntityMap> {
    /**
     * 设置${entity.name}对象id字段值
     */
    public transient final KeyValue<UserEntityMap> id = new KeyValue(this, "id");
    /**
     * 设置${entity.name}对象gmtCreated字段值
     */
    public transient final KeyValue<UserEntityMap> gmtCreated = new KeyValue(this, "gmtCreated");
    /**
     * 设置${entity.name}对象gmtModified字段值
     */
    public transient final KeyValue<UserEntityMap> gmtModified = new KeyValue(this, "gmtModified");
    /**
     * 设置${entity.name}对象isDeleted字段值
     */
    public transient final KeyValue<UserEntityMap> isDeleted = new KeyValue(this, "isDeleted");
    /**
     * 设置${entity.name}对象addressId字段值
     */
    public transient final KeyValue<UserEntityMap> addressId = new KeyValue(this, "addressId");
    /**
     * 设置${entity.name}对象age字段值
     */
    public transient final KeyValue<UserEntityMap> age = new KeyValue(this, "age");
    /**
     * 设置${entity.name}对象userName字段值
     */
    public transient final KeyValue<UserEntityMap> userName = new KeyValue(this, "userName");
    /**
     * 设置${entity.name}对象version字段值
     */
    public transient final KeyValue<UserEntityMap> version = new KeyValue(this, "version");

    public UserEntityMap() {
        super();
    }

    public UserEntityMap(int size) {
        super(size);
    }

    public static UserEntityMap create() {
        return new UserEntityMap();
    }

    public static UserEntityMap create(int size) {
        return new UserEntityMap(size);
    }

    public static class Factory {
        public UserEntityMap create() {
            return UserEntityMap.create();
        }

        public UserEntityMap create(int size) {
            return UserEntityMap.create(size);
        }
    }
}