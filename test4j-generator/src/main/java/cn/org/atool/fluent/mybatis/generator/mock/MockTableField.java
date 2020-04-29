package cn.org.atool.fluent.mybatis.generator.mock;

import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;

import static cn.org.atool.fluent.mybatis.generator.MybatisGenerator.currTable;

public class MockTableField extends MockUp<TableField> {
    @Mock
    public TableField setColumnType(Invocation it, final IColumnType columnType) {
        String name = ((TableField) it.getInvokedInstance()).getName();
        IColumnType specType = currTable().columnType(name);
        return it.proceed(specType == null ? columnType : specType);
    }
}
