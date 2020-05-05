package org.test4j.generator.mybatis.template.entity;

import org.test4j.generator.mybatis.model.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class EntityTemplate extends BaseTemplate {
    /**
     * entity类继承的接口全称
     */
    private List<String> interfaces = new ArrayList<>();

    public EntityTemplate() {
        super("templates/entity/Entity.java.vm", "entity/*Entity.java");
    }

    @Override
    protected String getTemplateId() {
        return KEY_ENTITY;
    }

    public EntityTemplate addInterface(String fullName) {
        this.interfaces.add(fullName);
        return this;
    }

    @Override
    protected Map<String, Object> templateConfigs(TableInfo table) {
        Map<String, Object> entity = new HashMap<>();
        if (!this.interfaces.isEmpty()) {
            entity.put("interface",
                this.interfaces.stream().map(i -> "import " + i).collect(joining(";\n"))
            );
            entity.put("interfaceName",
                this.interfaces.stream().map(i -> {
                    int last = i.lastIndexOf('.');
                    return i.substring(last + 1);
                }).collect(joining(" ,"))
            );
        }
        return entity;
    }
}
