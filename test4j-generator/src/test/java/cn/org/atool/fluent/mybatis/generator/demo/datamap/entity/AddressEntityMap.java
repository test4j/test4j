package cn.org.atool.fluent.mybatis.generator.demo.datamap.entity;

import cn.org.atool.fluent.mybatis.generator.demo.mapping.AddressMP.Property;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * @ClassName AddressEntityMap
 * @Description AddressEntityMap
 *
 * @author generate code
 */
public class AddressEntityMap extends DataMap<AddressEntityMap> {
    /**
     * 设置AddressEntity对象id字段值
     */
    public transient final KeyValue<AddressEntityMap> id = new KeyValue(this, Property.id);
    /**
     * 设置AddressEntity对象gmtCreated字段值
     */
    public transient final KeyValue<AddressEntityMap> gmtCreated = new KeyValue(this, Property.gmtCreated);
    /**
     * 设置AddressEntity对象gmtModified字段值
     */
    public transient final KeyValue<AddressEntityMap> gmtModified = new KeyValue(this, Property.gmtModified);
    /**
     * 设置AddressEntity对象isDeleted字段值
     */
    public transient final KeyValue<AddressEntityMap> isDeleted = new KeyValue(this, Property.isDeleted);
    /**
     * 设置AddressEntity对象address字段值
     */
    public transient final KeyValue<AddressEntityMap> address = new KeyValue(this, Property.address);

    public AddressEntityMap() {
        super();
    }

    public AddressEntityMap(int size) {
        super(size);
    }

    public static AddressEntityMap create() {
        return new AddressEntityMap();
    }

    public static AddressEntityMap create(int size) {
        return new AddressEntityMap(size);
    }

    public static class Factory {
        public AddressEntityMap create() {
            return AddressEntityMap.create();
        }

        public AddressEntityMap create(int size) {
            return AddressEntityMap.create(size);
        }
    }
}
