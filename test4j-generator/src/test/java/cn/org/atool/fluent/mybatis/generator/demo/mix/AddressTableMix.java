package cn.org.atool.fluent.mybatis.generator.demo.mix;

import cn.org.atool.fluent.mybatis.generator.demo.datamap.table.AddressTableMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

import static cn.org.atool.fluent.mybatis.generator.demo.mapping.AddressMP.Table_Name;

/**
 * 数据库address表数据准备和校验通用方法
 *
 * @author generate code
 */
public class AddressTableMix implements IMix {
    @Step("清空表[" + Table_Name + "]数据")
    public AddressTableMix cleanAddressTable() {
        db.table(Table_Name).clean();
        return this;
    }

    @Step("准备表[" + Table_Name + "]数据{1}")
    public AddressTableMix readyAddressTable(AddressTableMap data) {
        db.table(Table_Name).insert(data);
        return this;
    }

    @Step("验证表[" + Table_Name + "]有全表数据{1}")
    public AddressTableMix checkAddressTable(AddressTableMap data) {
        db.table(Table_Name).query().eqDataMap(data, EqMode.IGNORE_ORDER);
        return this;
    }

    @Step("验证表[" + Table_Name + "]有符合条件{1}的数据{2}")
    public AddressTableMix checkAddressTable(String where, AddressTableMap data) {
        db.table(Table_Name).queryWhere(where).eqDataMap(data, EqMode.IGNORE_ORDER);
        return this;
    }

    @Step("验证表[" + Table_Name + "]有符合条件{1}的数据{2}")
    public AddressTableMix checkAddressTable(AddressTableMap where, AddressTableMap data) {
        db.table(Table_Name).queryWhere(where).eqDataMap(data, EqMode.IGNORE_ORDER);
        return this;
    }

    @Step("验证表[" + Table_Name + "]有{1}条符合条件{2}的数据")
    public AddressTableMix countAddressTable(int count, AddressTableMap where) {
        db.table(Table_Name).queryWhere(where).sizeEq(count);
        return this;
    }

    @Step("验证表[" + Table_Name + "]有{1}条符合条件{2}的数据")
    public AddressTableMix countAddressTable(int count, String where) {
        db.table(Table_Name).queryWhere(where).sizeEq(count);
        return this;
    }

    @Step("验证表[" + Table_Name + "]有{1}条数据")
    public AddressTableMix countAddressTable(int count) {
        db.table(Table_Name).query().sizeEq(count);
        return this;
    }
}