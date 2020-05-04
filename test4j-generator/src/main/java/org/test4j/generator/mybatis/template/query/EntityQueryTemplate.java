package org.test4j.generator.mybatis.template.query;

import org.test4j.generator.mybatis.model.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class EntityQueryTemplate extends BaseTemplate {
    public EntityQueryTemplate() {
        super("templates/query/EntityQuery.java.vm", "query/*EntityQuery.java");
    }

    @Override
    protected String getTemplateId() {
        return "entityQuery";
    }

    @Override
    protected Map<String, Object> templateConfigs(TableInfo table) {
        return null;
    }
}
