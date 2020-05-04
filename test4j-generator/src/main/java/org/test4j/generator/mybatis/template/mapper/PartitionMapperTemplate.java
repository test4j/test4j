package org.test4j.generator.mybatis.template.mapper;

import org.test4j.generator.mybatis.model.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class PartitionMapperTemplate extends BaseTemplate {
    public PartitionMapperTemplate() {
        super("mapper/Partition.java.vm", "mapper/*PartitionMapper.java");
        super.setPartition(true);
    }

    @Override
    protected String getTemplateId() {
        return "partition";
    }

    @Override
    protected Map<String, Object> templateConfigs(TableInfo table) {
        return null;
    }
}
