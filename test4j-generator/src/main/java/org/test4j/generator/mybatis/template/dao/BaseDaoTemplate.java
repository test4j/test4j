package org.test4j.generator.mybatis.template.dao;

import org.test4j.generator.mybatis.config.constant.OutputDir;
import org.test4j.generator.mybatis.config.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class BaseDaoTemplate extends BaseTemplate {
    public BaseDaoTemplate() {
        super("templates/dao/BaseDao.java.vm", "dao/base/*BaseDao.java");
        this.outputDir = OutputDir.Base;
    }

    @Override
    public String getTemplateId() {
        return "baseDao";
    }

    @Override
    protected void templateConfigs(TableInfo table, Map<String, Object> context) {
    }
}
