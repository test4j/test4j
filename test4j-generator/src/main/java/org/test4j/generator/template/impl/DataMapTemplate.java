package org.test4j.generator.template.impl;

import org.test4j.generator.config.constant.OutputDir;
import org.test4j.generator.config.impl.TableSetter;
import org.test4j.generator.template.BaseTemplate;

import java.util.Map;

public class DataMapTemplate extends BaseTemplate {
    public DataMapTemplate() {
        super("templates/DataMap.java.vm", "dm/*DataMap.java");
        super.outputDir = OutputDir.Test;
    }

    @Override
    public String getTemplateId() {
        return "dataMap";
    }

    @Override
    protected void templateConfigs(TableSetter table, Map<String, Object> parent, Map<String, Object> ctx) {
        String name = table.getEntityPrefix();
        ctx.put("instance", name.substring(0, 1).toLowerCase() + name.substring(1));
    }
}