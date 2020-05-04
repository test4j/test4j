package org.test4j.generator.mybatis.template;

import org.test4j.generator.mybatis.model.TableInfo;

import java.util.Map;

public class PartitionMapperTemplate extends AbstractTableTemplate {
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
