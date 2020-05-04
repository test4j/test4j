package org.test4j.generator.mybatis.template;

import org.test4j.generator.mybatis.model.BuildConfig;
import org.test4j.generator.mybatis.model.TableInfo;
import org.test4j.tools.commons.StringHelper;

import java.util.HashMap;
import java.util.Map;

public class EntityTemplate extends AbstractTableTemplate {

    public EntityTemplate() {
        super("templates/entity/Entity.java.vm", "entity/*Entity.java");
    }

    @Override
    protected void templateConfigs(TableInfo table, Map<String, Object> configs) {
        String entityName = super.getFileName(table);
        configs.put(KEY_ENTITY_NAME, entityName);
        Map<String, Object> entity = new HashMap<>();
        {
            entity.put(KEY_NAME, entityName);
            entity.put(KEY_PACKAGE, super.getPackage(table));
        }
        configs.put(KEY_ENTITY, entity);
    }
}
