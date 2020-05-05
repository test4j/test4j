package org.test4j.generator.mybatis.template.dao;

import org.test4j.generator.mybatis.config.OutputDir;
import org.test4j.generator.mybatis.model.TableInfo;
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
    protected Map<String, Object> templateConfigs(TableInfo table) {
        return null;
    }
}
