package org.test4j.generator.mybatis.template.dao;

import org.test4j.generator.mybatis.config.OutputDir;
import org.test4j.generator.mybatis.model.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class DaoImplTemplate extends BaseTemplate {
    public DaoImplTemplate() {
        super("templates/dao/DaoImpl.java.vm", "dao/impl/*DaoImpl.java");
        super.outputDir = OutputDir.Dao;
    }

    @Override
    public String getTemplateId() {
        return "daoImpl";
    }

    @Override
    protected void templateConfigs(TableInfo table, Map<String, Object> context) {
    }
}
