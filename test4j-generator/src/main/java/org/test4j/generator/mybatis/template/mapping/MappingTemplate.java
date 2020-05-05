package org.test4j.generator.mybatis.template.mapping;

import org.test4j.generator.mybatis.model.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.test4j.generator.mybatis.config.ConfigKey.KEY_MAPPING;

public class MappingTemplate extends BaseTemplate {
    public MappingTemplate() {
        super("templates/mapping/TableMapping.java.vm", "mapping/*MP.java");
    }

    @Override
    public String getTemplateId() {
        return KEY_MAPPING;
    }

    @Override
    protected Map<String, Object> templateConfigs(TableInfo table) {
        return null;
    }
}
