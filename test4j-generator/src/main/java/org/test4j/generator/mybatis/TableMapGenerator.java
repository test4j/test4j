package org.test4j.generator.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.test4j.generator.mybatis.template.BaseTemplate;
import org.test4j.generator.mybatis.template.TemplateList;

import java.util.List;

/**
 * DataMapGenerator
 *
 * @author darui.wu
 */
@Slf4j
public class TableMapGenerator extends BaseGenerator<TableMapGenerator> {

    public static TableMapGenerator build() {
        return new TableMapGenerator();
    }

    @Override
    protected List<BaseTemplate> getAllTemplates() {
        return TemplateList.ONLY_TABLE_MAP;
    }
}