package org.test4j.generator.mybatis.template.dao;

import org.test4j.generator.mybatis.model.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class DaoImplTemplate extends BaseTemplate {
    public DaoImplTemplate() {
        super("templates/dao/DaoImpl.java.vm", "dao/impl/*DaoImpl.java");
    }

    @Override
    protected String getTemplateId() {
        return "daoImpl";
    }

    @Override
    protected Map<String, Object> templateConfigs(TableInfo table) {
        return null;
    }
}
