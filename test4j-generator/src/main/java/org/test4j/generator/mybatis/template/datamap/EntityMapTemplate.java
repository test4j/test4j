package org.test4j.generator.mybatis.template.datamap;

import org.test4j.generator.mybatis.config.OutputDir;
import org.test4j.generator.mybatis.model.TableInfo;
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
    protected Map<String, Object> templateConfigs(TableInfo table) {
        return null;
    }
}
