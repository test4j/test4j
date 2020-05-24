package org.test4j.generator.mybatis.template.mapper;

import org.test4j.generator.mybatis.config.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;
import org.test4j.tools.commons.StringHelper;

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
        if (!StringHelper.isBlank(table.getMapperBeanPrefix())) {
            context.put("prefix", table.getMapperBeanPrefix());
        }
    }
}