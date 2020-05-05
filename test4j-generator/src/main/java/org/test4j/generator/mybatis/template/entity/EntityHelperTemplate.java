package org.test4j.generator.mybatis.template.entity;

import org.test4j.generator.mybatis.model.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.HashMap;
import java.util.Map;


public class EntityHelperTemplate extends BaseTemplate {
    public EntityHelperTemplate() {
        super("templates/entity/EntityHelper.java.vm", "helper/*EntityHelper.java");
    }

    @Override
    public String getTemplateId() {
        return "entityHelper";
    }

    @Override
    protected Map<String, Object> templateConfigs(TableInfo table) {
        return null;
    }
}
