package org.test4j.generator.mybatis.template.mapper;

import org.test4j.generator.mybatis.model.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class MapperTemplate extends BaseTemplate {
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