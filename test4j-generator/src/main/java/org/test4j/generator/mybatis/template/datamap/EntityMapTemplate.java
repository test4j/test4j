package org.test4j.generator.mybatis.template.datamap;

import org.test4j.generator.mybatis.config.constant.OutputDir;
import org.test4j.generator.mybatis.config.impl.TableSetter;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class EntityMapTemplate extends BaseTemplate {
    public EntityMapTemplate() {
        super("templates/datamap/EntityMap.java.vm", "datamap/entity/*EntityMap.java");
        super.outputDir = OutputDir.Test;
    }

    @Override
    public String getTemplateId() {
        return "entityMap";
    }

    @Override
    protected void templateConfigs(TableSetter table, Map<String, Object> parent, Map<String, Object> ctx) {
        String name = table.getEntityPrefix();
        ctx.put("instance", name.substring(0, 1).toLowerCase() + name.substring(1));
    }
}