package org.test4j.generator.mybatis.model;

import org.test4j.generator.mybatis.template.AbstractTableTemplate;
import org.test4j.generator.mybatis.template.EntityHelperTemplate;
import org.test4j.generator.mybatis.template.EntityTemplate;
import org.test4j.generator.mybatis.template.MappingTemplate;

import java.util.Arrays;
import java.util.List;

public interface TemplateList {
    List<AbstractTableTemplate> templates = Arrays.asList(
        new MappingTemplate(),
        new EntityTemplate(),
        new EntityHelperTemplate()
    );

}
