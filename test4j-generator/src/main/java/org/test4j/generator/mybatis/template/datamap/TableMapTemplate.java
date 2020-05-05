package org.test4j.generator.mybatis.template.datamap;

import org.test4j.generator.mybatis.model.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class TableMapTemplate extends BaseTemplate {
    public TableMapTemplate() {
        super("templates/datamap/TableMap.java.vm", "datamap/table/*TableMap.java");
        super.outputDir = OutputDir.Test;
    }

    @Override
    protected String getTemplateId() {
        return "tableMap";
    }

    @Override
    protected Map<String, Object> templateConfigs(TableInfo table) {
        return null;
    }
}
