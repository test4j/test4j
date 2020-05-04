package org.test4j.generator.mybatis.template;

import org.test4j.generator.mybatis.model.TableInfo;

import java.util.Map;

public class BaseDaoTemplate extends AbstractTableTemplate {
    public BaseDaoTemplate() {
        super("templates/dao/BaseDao.java.vm", "dao/base/*BaseDao.java");
        super.setBaseDao(true);
    }

    @Override
    protected String getTemplateId() {
        return "baseDao";
    }

    @Override
    protected Map<String, Object> templateConfigs(TableInfo table) {
        return null;
    }
}
