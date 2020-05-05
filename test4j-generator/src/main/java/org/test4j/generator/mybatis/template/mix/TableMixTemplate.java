package org.test4j.generator.mybatis.template.mix;

import org.test4j.generator.mybatis.config.OutputDir;
import org.test4j.generator.mybatis.model.TableInfo;
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
    protected void templateConfigs(TableInfo table, Map<String, Object> context) {
        String name = (String) context.get("name");
        context.put("instance", name.substring(0, 1).toLowerCase() + name.substring(1));
        context.put("cleanMethod", String.format("clean%sTable", table.getEntityPrefix()));
    }
}
