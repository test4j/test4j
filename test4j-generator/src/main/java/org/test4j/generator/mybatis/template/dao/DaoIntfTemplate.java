package org.test4j.generator.mybatis.template.dao;

import org.test4j.generator.mybatis.model.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class DaoIntfTemplate extends BaseTemplate {
    public DaoIntfTemplate() {
        super("templates/dao/DaoIntf.java.vm", "dao/intf/*Dao.java");
    }

    @Override
    protected String getTemplateId() {
        return "daoIntf";
    }

    @Override
    protected Map<String, Object> templateConfigs(TableInfo table) {
        return null;
    }
}
