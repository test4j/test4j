package org.test4j.generator.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.test4j.generator.mybatis.template.BaseTemplate;
import org.test4j.generator.mybatis.template.DataMapTemplateList;
import org.test4j.generator.mybatis.template.summary.SummaryTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DataMapGenerator
 *
 * @author darui.wu
 */
@Slf4j
public class DataMapGenerator extends BaseTemplateGenerator {

    public static IGlobalConfig build() {
        return new DataMapGenerator();
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
        for (SummaryTemplate summary : DataMapTemplateList.summaries) {
            summary.setGlobalConfig(this.globalConfig);
            templateEngine.output(summary.getTemplateId(), wrapper, summary.getFilePath());
        }
    }

    @Override
    protected List<BaseTemplate> getAllTemplates() {
        return DataMapTemplateList.datamap_list;
    }
}