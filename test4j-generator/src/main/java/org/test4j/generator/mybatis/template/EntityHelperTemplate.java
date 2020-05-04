package org.test4j.generator.mybatis.template;

import org.test4j.generator.mybatis.model.TableInfo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class EntityHelperTemplate extends AbstractTableTemplate {
    public EntityHelperTemplate() {
        super("templates/entity/EntityHelper.java.vm", "helper/*EntityHelper.java");
    }

    @Override
    protected String getTemplateId() {
        return "entityHelper";
    }

    @Override
    protected Map<String, Object> templateConfigs(TableInfo table) {
        Map<String, Object> helper = new HashMap<>();
        {
            helper.put(KEY_NAME, super.getFileName(table));
            helper.put(KEY_PACKAGE, super.getPackage(table));
        }
        {
            List<String> types = table.getFieldTypes();
            helper.put("importTypes",
                types.stream().map(type -> "import " + type + ";").sorted().collect(joining("\n"))
            );
        }
        return helper;
    }
}
