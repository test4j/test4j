package org.test4j.generator.mybatis.template;

import org.test4j.generator.mybatis.config.OutputDir;
import org.test4j.generator.mybatis.template.dao.BaseDaoTemplate;
import org.test4j.generator.mybatis.template.dao.DaoImplTemplate;
import org.test4j.generator.mybatis.template.dao.DaoIntfTemplate;
import org.test4j.generator.mybatis.template.datamap.EntityMapTemplate;
import org.test4j.generator.mybatis.template.datamap.TableMapTemplate;
import org.test4j.generator.mybatis.template.entity.EntityHelperTemplate;
import org.test4j.generator.mybatis.template.entity.EntityTemplate;
import org.test4j.generator.mybatis.template.mapper.MapperTemplate;
import org.test4j.generator.mybatis.template.mapper.PartitionMapperTemplate;
import org.test4j.generator.mybatis.template.mapping.MappingTemplate;
import org.test4j.generator.mybatis.template.mix.TableMixTemplate;
import org.test4j.generator.mybatis.template.query.EntityQueryTemplate;
import org.test4j.generator.mybatis.template.query.EntityUpdateTemplate;
import org.test4j.generator.mybatis.template.query.EntityWrapperHelperTemplate;

import java.util.Arrays;
import java.util.List;

public interface TemplateList {
    List<BaseTemplate> ALL_TEMPLATES = Arrays.asList(
        new MappingTemplate(),
        new EntityTemplate(),
        new EntityHelperTemplate(),
        new MapperTemplate(),
        new PartitionMapperTemplate(),
        new EntityWrapperHelperTemplate(),
        new EntityQueryTemplate(),
        new EntityUpdateTemplate(),
        new BaseDaoTemplate(),
        new DaoIntfTemplate(),
        new DaoImplTemplate(),
        new EntityMapTemplate(),
        new TableMapTemplate(),
        new TableMixTemplate()
    );

    List<BaseTemplate> ONLY_TEST = Arrays.asList(
        new MappingTemplate().setOutputDir(OutputDir.Test),
        new EntityMapTemplate(),
        new TableMapTemplate(),
        new TableMixTemplate()
    );
}
