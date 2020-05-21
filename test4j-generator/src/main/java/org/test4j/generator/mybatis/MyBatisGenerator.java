package org.test4j.generator.mybatis;


import lombok.extern.slf4j.Slf4j;
import org.test4j.generator.mybatis.config.TableConfig;
import org.test4j.generator.mybatis.template.BaseTemplate;
import org.test4j.generator.mybatis.template.TemplateList;
import org.test4j.generator.mybatis.template.summary.SummaryTemplate;

import java.util.*;

/**
 * 数据库表列表
 *
 * @author wudarui
 */
@Slf4j
public class MyBatisGenerator extends BaseGenerator {

    public MyBatisGenerator(TableConfig... tableConfigs) {
        super(tableConfigs);
    }

    @Override
    protected List<BaseTemplate> getAllTemplates() {
        return TemplateList.ALL_TEMPLATES;
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
        for (SummaryTemplate summary : TemplateList.summaries) {
            summary.setGlobalConfig(this.globalConfig);
            templateEngine.output(summary.getTemplateId(), wrapper, summary.getFilePath());
        }
    }
}