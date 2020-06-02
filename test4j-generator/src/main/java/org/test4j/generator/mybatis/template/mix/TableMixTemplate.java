package org.test4j.generator.mybatis.template.mix;

import org.test4j.generator.mybatis.config.constant.OutputDir;
import org.test4j.generator.mybatis.config.impl.TableInfoSet;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class TableMixTemplate extends BaseTemplate {
    public TableMixTemplate() {
        super("templates/mix/TableMix.java.vm", "mix/*TableMix.java");
        super.outputDir = OutputDir.Test;
    }

    @Override
    public String getTemplateId() {
        return "tableMix";
    }

    @Override
    protected void templateConfigs(TableInfoSet table, Map<String, Object> templateContext) {
        String name = table.getEntityPrefix();
        templateContext.put("instance", name.substring(0, 1).toLowerCase() + name.substring(1) + "TableMix");
        templateContext.put("cleanMethod", String.format("clean%sTable", table.getEntityPrefix()));
    }
}