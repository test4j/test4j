package cn.org.atool.fluent.mybatis.generator.demo.mix;

import cn.org.atool.fluent.mybatis.generator.demo.dm.NoAutoIdDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库no_auto_id表数据准备和校验通用方法
 *
 * @author generate code
 */
public class NoAutoIdTableMix implements IMix {
    @Step("清空表[no_auto_id]数据")
    public NoAutoIdTableMix cleanNoAutoIdTable() {
        db.table("no_auto_id").clean();
        return this;
    }

    @Step("准备表[no_auto_id]数据{1}")
    public NoAutoIdTableMix readyNoAutoIdTable(NoAutoIdDataMap data) {
        db.table("no_auto_id").insert(data);
        return this;
    }

    @Step("验证表[no_auto_id]有全表数据{1}")
    public NoAutoIdTableMix checkNoAutoIdTable(NoAutoIdDataMap data) {
        db.table("no_auto_id").query().eqDataMap(data, EqMode.IGNORE_ORDER);
        return this;
    }

    @Step("验证表[no_auto_id]有符合条件{1}的数据{2}")
    public NoAutoIdTableMix checkNoAutoIdTable(String where, NoAutoIdDataMap data) {
        db.table("no_auto_id").queryWhere(where).eqDataMap(data, EqMode.IGNORE_ORDER);
        return this;
    }

    @Step("验证表[no_auto_id]有符合条件{1}的数据{2}")
    public NoAutoIdTableMix checkNoAutoIdTable(NoAutoIdDataMap where, NoAutoIdDataMap data) {
        db.table("no_auto_id").queryWhere(where).eqDataMap(data, EqMode.IGNORE_ORDER);
        return this;
    }

    @Step("验证表[no_auto_id]有{1}条符合条件{2}的数据")
    public NoAutoIdTableMix countNoAutoIdTable(int count, NoAutoIdDataMap where) {
        db.table("no_auto_id").queryWhere(where).sizeEq(count);
        return this;
    }

    @Step("验证表[no_auto_id]有{1}条符合条件{2}的数据")
    public NoAutoIdTableMix countNoAutoIdTable(int count, String where) {
        db.table("no_auto_id").queryWhere(where).sizeEq(count);
        return this;
    }

    @Step("验证表[no_auto_id]有{1}条数据")
    public NoAutoIdTableMix countNoAutoIdTable(int count) {
        db.table("no_auto_id").query().sizeEq(count);
        return this;
    }
}