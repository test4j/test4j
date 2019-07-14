package org.test4j.module;

import org.test4j.module.database.utility.EntityScriptParser;

public interface IScript {
    /**
     * 生成数据库脚本
     * @return
     */
    String script();

    default String script(Class ... klasses){
        return EntityScriptParser.script(klasses);
    }
}
