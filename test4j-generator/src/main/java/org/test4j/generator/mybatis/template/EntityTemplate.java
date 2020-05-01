package org.test4j.generator.mybatis.template;

import org.test4j.generator.mybatis.config.INameConvert;
import org.test4j.generator.mybatis.model.BuildConfig;
import org.test4j.generator.mybatis.model.TableInfo;
import org.test4j.generator.mybatis.rule.Naming;
import org.test4j.tools.commons.StringHelper;

import java.util.Map;

public class EntityTemplate extends AbstractTableTemplate {
    String ENTITY = "Entity";

    private String entityName;

    private String entityFormat = "%sEntity";

    public EntityTemplate() {
        super("entity/Entity.java.vm", "entity/*Entity.java");
    }

    @Override
    public void generate(BuildConfig config, TableInfo tableInfo) {
        this.entityName = tableInfo.getEntityPrefix();
        if (!StringHelper.isBlank(entityFormat) && entityFormat.contains("%s")) {
            this.entityName = String.format(entityFormat, entityName);
        }
    }

    @Override
    public Map<String, Object> initWith(TableInfo table) {
        return null;
    }


}
