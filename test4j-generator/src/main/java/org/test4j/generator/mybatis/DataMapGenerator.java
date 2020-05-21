package org.test4j.generator.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.test4j.generator.mybatis.config.TableConfig;
import org.test4j.generator.mybatis.template.BaseTemplate;
import org.test4j.generator.mybatis.template.TemplateList;

import java.util.List;

@Slf4j
public class DataMapGenerator extends BaseGenerator {
    public DataMapGenerator(TableConfig... tableConfigs) {
        super(tableConfigs);
    }

    @Override
    protected List<BaseTemplate> getAllTemplates() {
        return TemplateList.ONLY_TABLE_MAP;
    }
}