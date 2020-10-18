package org.test4j.generator.template.impl;

import org.test4j.generator.config.constant.OutputDir;
import org.test4j.generator.config.impl.TableSetter;
import org.test4j.generator.template.BaseTemplate;

import java.util.Map;

import static org.test4j.generator.config.constant.ConfigKey.KEY_OVER_WRITE;

public class DaoImplTemplate extends BaseTemplate {
    public DaoImplTemplate() {
        super("templates/DaoImpl.java.vm", "dao/impl/*DaoImpl.java");
        super.outputDir = OutputDir.Dao;
    }

    @Override
    public String getTemplateId() {
        return "daoImpl";
    }

    @Override
    protected void templateConfigs(TableSetter table, Map<String, Object> parent, Map<String, Object> context) {
        context.put("baseDaoName", table.getEntityPrefix() + "BaseDao");
        context.put("baseDaoPack", table.getBasePackage() + ".dao.base");
        context.put(KEY_OVER_WRITE, Boolean.FALSE.toString());
    }

    @Override
    protected String getPackage(TableSetter table) {
        int index = this.fileNameReg.lastIndexOf('/');
        String sub = "";
        if (index > 0) {
            sub = this.fileNameReg.substring(0, index).replace('/', '.');
        }
        return table.getDaoPackage() + "." + sub;
    }
}