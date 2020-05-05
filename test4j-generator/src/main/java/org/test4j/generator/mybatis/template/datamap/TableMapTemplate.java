package org.test4j.generator.mybatis.template.datamap;

import org.test4j.generator.mybatis.config.constant.OutputDir;
import org.test4j.generator.mybatis.config.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class TableMapTemplate extends BaseTemplate {
    public TableMapTemplate() {
        super("templates/datamap/TableMap.java.vm", "datamap/table/*TableMap.java");
        super.outputDir = OutputDir.Test;
    }

    @Override
    public String getTemplateId() {
        return "tableMap";
    }

    @Override
    protected void templateConfigs(TableInfo table, Map<String, Object> context) {
    }
}
