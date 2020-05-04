package org.test4j.generator.mybatis.model;

import org.test4j.generator.mybatis.template.*;

import java.util.Arrays;
import java.util.List;

public interface TemplateList {
    List<AbstractTableTemplate> templates = Arrays.asList(
        new MappingTemplate(),
        new EntityTemplate(),
        new EntityHelperTemplate(),
        new MapperTemplate(),
        new PartitionMapperTemplate(),
        new BaseDaoTemplate()
    );

}
