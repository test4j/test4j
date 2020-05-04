package org.test4j.generator.mybatis.template;

import org.test4j.generator.mybatis.model.TableInfo;

import java.util.HashMap;
import java.util.Map;

public class MappingTemplate extends AbstractTableTemplate {
    public MappingTemplate() {
        super("templates/mapping/TableMapping.java.vm", "mapping/*MP.java");
    }

    @Override
    protected void templateConfigs(TableInfo table, Map<String, Object> configs) {
        String mappingName = super.getFileName(table);
        Map<String, Object> mapping = new HashMap<>();
        {
            mapping.put(KEY_NAME, mappingName);
            mapping.put(KEY_PACKAGE, super.getPackage(table));
        }
        configs.put(KEY_MAPPING, mapping);
    }
}
