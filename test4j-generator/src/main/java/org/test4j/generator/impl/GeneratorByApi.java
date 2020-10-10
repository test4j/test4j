package org.test4j.generator.impl;

import lombok.extern.slf4j.Slf4j;
import org.test4j.generator.config.IGlobalConfig;
import org.test4j.generator.template.BaseTemplate;
import org.test4j.generator.template.DataMapTemplateList;
import org.test4j.generator.template.summary.SummaryTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DataMapGenerator
 *
 * @author darui.wu
 */
@Slf4j
public class GeneratorByApi extends BaseTemplateGenerator {
    /**
     * 生成test辅助文件
     */
    private boolean withTest = false;
    /**
     * 生成entity文件
     */
    private boolean withEntity = false;


    public static IGlobalConfig build(boolean withEntity, boolean withTest) {
        if (!withEntity && !withTest) {
            throw new RuntimeException("生成Entity和Test辅助类不能同时为假");
        }
        GeneratorByApi generator = new GeneratorByApi();
        generator.withTest = withTest;
        generator.withEntity = withEntity;
        return generator;
    }

    /**
     * 生成汇总文件
     *
     * @param allContext
     */
    @Override
    protected void generateSummary(List<Map<String, Object>> allContext) {
        Map<String, Object> wrapper = new HashMap<>();
        {
            wrapper.put("configs", allContext);
            wrapper.put("basePackage", this.globalConfig.getBasePackage());
        }

        if (!withTest) {
            return;
        }
        for (SummaryTemplate summary : DataMapTemplateList.summaries) {
            summary.setGlobalConfig(this.globalConfig);
            templateEngine.output(summary.getTemplateId(), wrapper, summary.getFilePath());
        }
    }

    @Override
    protected List<BaseTemplate> getAllTemplates() {
        List<BaseTemplate> list = new ArrayList<>();
        if (withTest) {
            list.addAll(DataMapTemplateList.test_list);
        }
        if (withEntity) {
            list.addAll(DataMapTemplateList.entity_list);
        }
        return list;
    }
}