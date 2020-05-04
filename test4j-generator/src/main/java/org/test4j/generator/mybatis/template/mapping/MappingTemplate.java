package org.test4j.generator.mybatis.template.mapping;

import org.test4j.generator.mybatis.model.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.HashMap;
import java.util.Map;

public class MappingTemplate extends BaseTemplate {
    public MappingTemplate() {
        super("templates/mapping/TableMapping.java.vm", "mapping/*MP.java");
    }

    @Override
    protected String getTemplateId() {
        return KEY_MAPPING;
    }

    @Override
    protected Map<String, Object> templateConfigs(TableInfo table) {
        String mappingName = super.getFileName(table);
        Map<String, Object> mapping = new HashMap<>();
        {
            mapping.put(KEY_NAME, mappingName);
            mapping.put(KEY_PACKAGE, super.getPackage(table));
        }
        return mapping;
    }
}
