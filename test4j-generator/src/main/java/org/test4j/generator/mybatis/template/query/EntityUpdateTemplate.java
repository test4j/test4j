package org.test4j.generator.mybatis.template.query;

import org.test4j.generator.mybatis.model.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class EntityUpdateTemplate extends BaseTemplate {
    public EntityUpdateTemplate() {
        super("templates/query/EntityUpdate.java.vm", "query/*EntityUpdate.java");
    }

    @Override
    public String getTemplateId() {
        return "entityUpdate";
    }

    @Override
    protected Map<String, Object> templateConfigs(TableInfo table) {
        return null;
    }
}
