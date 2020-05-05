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
    protected Map<String, Object> templateConfigs(TableInfo table) {
        return null;
    }
}
