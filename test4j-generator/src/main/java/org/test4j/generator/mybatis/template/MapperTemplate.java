package org.test4j.generator.mybatis.template;

import org.test4j.generator.mybatis.model.TableInfo;

import java.util.Map;

public class MapperTemplate extends AbstractTableTemplate {
    public MapperTemplate() {
        super("templates/mapper/Mapper.java.vm", "mapper/*Mapper.java");
    }

    @Override
    protected String getTemplateId() {
        return "mapper";
    }

    @Override
    protected Map<String, Object> templateConfigs(TableInfo table) {
        return null;
    }
}
