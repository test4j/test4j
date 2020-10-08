package org.test4j.generator.mybatis.template.mix;

import org.test4j.generator.mybatis.config.constant.OutputDir;
import org.test4j.generator.mybatis.config.impl.TableSetter;
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
    protected void templateConfigs(TableSetter table, Map<String, Object> parent, Map<String, Object> ctx) {
        String name = table.getEntityPrefix();
        ctx.put("instance", name.substring(0, 1).toLowerCase() + name.substring(1) + "TableMix");
        ctx.put("cleanMethod", String.format("clean%sTable", table.getEntityPrefix()));
    }
}