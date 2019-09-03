package org.test4j.module;

import org.test4j.module.database.utility.EntityScriptParser;

public interface IScript {
    /**
     * 生成数据库脚本
     *
     * @return
     */
    String script();

    /**
     * 构造测试库字段类型和原生库字段类型映射器
     *
     * @return
     */
    default EntityScriptParser.DbTypeConvert dbTypeConvert() {
        return new EntityScriptParser.NonDbTypeConvert();
    }

    /**
     * 根据klasses定义生成数据库脚本
     *
     * @param klasses
     * @return
     */
    default String script(Class... klasses) {
        return EntityScriptParser.script(dbTypeConvert(), klasses);
    }
}
