package org.test4j.db.dm;

import org.test4j.ICore.DataMap;
import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.tools.datagen.KeyValue;

import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @ClassName UserDataMap
 * @Description UserDataMap
 *
 * @author generate code
 */
@ScriptTable("t_user")
public class UserDataMap extends DataMap<UserDataMap> {
    private boolean isTable;

    private Supplier<Boolean> supplier = () -> this.isTable;

    @ColumnDef(value = "id", type = "bigint(21) unsigned", primary = true, autoIncrease = true)
    public transient final KeyValue<UserDataMap> id = new KeyValue(this, "id", "id", supplier);

    @ColumnDef(value = "gmt_created", type = "datetime")
    public transient final KeyValue<UserDataMap> gmtCreated = new KeyValue(this, "gmt_created", "gmtCreated", supplier);

    @ColumnDef(value = "gmt_modified", type = "datetime")
    public transient final KeyValue<UserDataMap> gmtModified = new KeyValue(this, "gmt_modified", "gmtModified", supplier);

    @ColumnDef(value = "is_deleted", type = "tinyint(2)")
    public transient final KeyValue<UserDataMap> isDeleted = new KeyValue(this, "is_deleted", "isDeleted", supplier);

    @ColumnDef(value = "address_id", type = "bigint(21)")
    public transient final KeyValue<UserDataMap> addressId = new KeyValue(this, "address_id", "addressId", supplier);

    @ColumnDef(value = "age", type = "int(11)")
    public transient final KeyValue<UserDataMap> age = new KeyValue(this, "age", "age", supplier);

    @ColumnDef(value = "e_mail", type = "varchar(100)")
    public transient final KeyValue<UserDataMap> eMail = new KeyValue(this, "e_mail", "eMail", supplier);

    @ColumnDef(value = "first_name", type = "varchar(50)")
    public transient final KeyValue<UserDataMap> firstName = new KeyValue(this, "first_name", "firstName", supplier);

    @ColumnDef(value = "grade", type = "int(11)")
    public transient final KeyValue<UserDataMap> grade = new KeyValue(this, "grade", "grade", supplier);

    @ColumnDef(value = "last_name", type = "varchar(50)")
    public transient final KeyValue<UserDataMap> lastName = new KeyValue(this, "last_name", "lastName", supplier);

    @ColumnDef(value = "post_code", type = "varchar(100)")
    public transient final KeyValue<UserDataMap> postCode = new KeyValue(this, "post_code", "postCode", supplier);

    @ColumnDef(value = "user_name", type = "varchar(45)")
    public transient final KeyValue<UserDataMap> userName = new KeyValue(this, "user_name", "userName", supplier);

    @ColumnDef(value = "version", type = "varchar(45)")
    public transient final KeyValue<UserDataMap> version = new KeyValue(this, "version", "version", supplier);

    public UserDataMap(boolean isTable) {
        super();
        this.isTable = isTable;
    }

    public UserDataMap(boolean isTable, int size) {
        super(size);
        this.isTable = isTable;
    }

    /**
     * 创建UserDataMap
     * 并初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
     *
     */
    public UserDataMap init() {
        this.id.autoIncrease();
        this.gmtCreated.values(new Date());
        this.gmtModified.values(new Date());
        this.isDeleted.values(false);
        return this;
    }

    public UserDataMap with(Consumer<UserDataMap> init) {
        init.accept(this);
        return this;
    }

    public static UserDataMap table() {
        return new UserDataMap(true, 1);
    }

    public static UserDataMap table(int size) {
        return new UserDataMap(true, size);
    }

    public static UserDataMap entity() {
        return new UserDataMap(false);
    }

    public static UserDataMap entity(int size) {
        return new UserDataMap(false, size);
    }

    public static class Factory {
        public UserDataMap table() {
            return UserDataMap.table();
        }

        public UserDataMap table(int size) {
            return UserDataMap.table(size);
        }

        public UserDataMap initTable() {
            return UserDataMap.table(1).init();
        }

        public UserDataMap initTable(int size) {
            return UserDataMap.table(size).init();
        }

        public UserDataMap entity() {
            return UserDataMap.entity();
        }

        public UserDataMap entity(int size) {
            return UserDataMap.entity(size);
        }
    }
}