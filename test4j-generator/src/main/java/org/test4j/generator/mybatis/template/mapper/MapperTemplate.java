package org.test4j.generator.mybatis.template.mapper;

import org.test4j.generator.mybatis.model.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class MapperTemplate extends BaseTemplate {
    public MapperTemplate() {
        super("templates/mapper/Mapper.java.vm", "mapper/*Mapper.java");
    }

    @Override
    public String getTemplateId() {
        return "mapper";
    }

    @Override
    protected void templateConfigs(TableInfo table, Map<String, Object> context) {
    }
}
