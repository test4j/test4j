package cn.org.atool.fluent.mybatis.generator.demo;

import cn.org.atool.fluent.mybatis.generator.demo.mix.*;
import org.test4j.module.spec.annotations.Mix;
import org.test4j.module.spec.annotations.Mixes;

/**
 * Table Mix工具聚合
 *
 * @author generate code
 */
@Mixes
public class TableMixes {
    @Mix
    public ${obj.mix} addressTableMix;

    @Mix
    public ${obj.mix} tUserTableMix;

    @Mix
    public ${obj.mix} noPrimaryTableMix;

    @Mix
    public ${obj.mix} noAutoIdTableMix;

    public void cleanAllTable() {
        this.addressTableMix.cleanAddressTable();
        this.tUserTableMix.cleanTUserTable();
        this.noPrimaryTableMix.cleanNoPrimaryTable();
        this.noAutoIdTableMix.cleanNoAutoIdTable();
    }
}