package org.test4j.generator.mybatis.template.query;

import org.test4j.generator.mybatis.model.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class EntityWrapperHelperTemplate extends BaseTemplate {
    public EntityWrapperHelperTemplate() {
        super("templates/query/EntityWrapperHelper.java.vm", "query/*EntityWrapperHelper.java");
    }

    @Override
    protected String getTemplateId() {
        return "wrapperHelper";
    }

    @Override
    protected Map<String, Object> templateConfigs(TableInfo table) {
        Map<String, Object> wrapper = new HashMap<>();
        {
            List<String> types = table.getFieldTypes();
            wrapper.put("importTypes",
                types.stream().map(type -> "import " + type + ";").sorted().collect(joining("\n"))
            );
        }
        return wrapper;
    }
}
