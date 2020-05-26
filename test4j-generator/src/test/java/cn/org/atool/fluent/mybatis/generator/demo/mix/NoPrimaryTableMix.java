package cn.org.atool.fluent.mybatis.generator.demo.mix;

import cn.org.atool.fluent.mybatis.generator.demo.datamap.table.NoPrimaryTableMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库no_primary表数据准备和校验通用方法
 *
 * @author generate code
 */
public class NoPrimaryTableMix implements IMix {
    @Step("清空表[no_primary]数据")
    public NoPrimaryTableMix cleanNoPrimaryTable() {
        db.table("no_primary").clean();
        return this;
    }

    @Step("准备表[no_primary]数据{1}")
    public NoPrimaryTableMix readyNoPrimaryTable(NoPrimaryTableMap data) {
        db.table("no_primary").insert(data);
        return this;
    }

    @Step("验证表[no_primary]有全表数据{1}")
    public NoPrimaryTableMix checkNoPrimaryTable(NoPrimaryTableMap data) {
        db.table("no_primary").query().eqDataMap(data, EqMode.IGNORE_ORDER);
        return this;
    }

    @Step("验证表[no_primary]有符合条件{1}的数据{2}")
    public NoPrimaryTableMix checkNoPrimaryTable(String where, NoPrimaryTableMap data) {
        db.table("no_primary").queryWhere(where).eqDataMap(data, EqMode.IGNORE_ORDER);
        return this;
    }

    @Step("验证表[no_primary]有符合条件{1}的数据{2}")
    public NoPrimaryTableMix checkNoPrimaryTable(NoPrimaryTableMap where, NoPrimaryTableMap data) {
        db.table("no_primary").queryWhere(where).eqDataMap(data, EqMode.IGNORE_ORDER);
        return this;
    }

    @Step("验证表[no_primary]有{1}条符合条件{2}的数据")
    public NoPrimaryTableMix countNoPrimaryTable(int count, NoPrimaryTableMap where) {
        db.table("no_primary").queryWhere(where).sizeEq(count);
        return this;
    }

    @Step("验证表[no_primary]有{1}条符合条件{2}的数据")
    public NoPrimaryTableMix countNoPrimaryTable(int count, String where) {
        db.table("no_primary").queryWhere(where).sizeEq(count);
        return this;
    }

    @Step("验证表[no_primary]有{1}条数据")
    public NoPrimaryTableMix countNoPrimaryTable(int count) {
        db.table("no_primary").query().sizeEq(count);
        return this;
    }
}