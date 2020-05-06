package org.test4j.generator.mybatis;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.test4j.generator.engine.AbstractTemplateEngine;
import org.test4j.generator.engine.VelocityTemplateEngine;
import org.test4j.generator.mybatis.config.TableConfig;
import org.test4j.generator.mybatis.config.GlobalConfig;
import org.test4j.generator.mybatis.config.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;
import org.test4j.generator.mybatis.template.TemplateList;
import org.test4j.generator.mybatis.template.summary.SummaryTemplate;
import org.test4j.hamcrest.Assert;
import org.test4j.tools.commons.StringHelper;

import java.util.*;

import static org.test4j.generator.mybatis.config.constant.ConfigKey.*;
import static org.test4j.module.core.utility.MessageHelper.info;

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
