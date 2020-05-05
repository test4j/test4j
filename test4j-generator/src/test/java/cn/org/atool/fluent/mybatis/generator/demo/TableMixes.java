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
    public AddressTableMix addressTableMix;

    @Mix
    public TUserTableMix tUserTableMix;

    @Mix
    public NoPrimaryTableMix noPrimaryTableMix;

    @Mix
    public NoAutoIdTableMix noAutoIdTableMix;

    public void cleanAllTable() {
        this.addressTableMix.cleanAddressTable();
        this.tUserTableMix.cleanTUserTable();
        this.noPrimaryTableMix.cleanNoPrimaryTable();
        this.noAutoIdTableMix.cleanNoAutoIdTable();
    }
}