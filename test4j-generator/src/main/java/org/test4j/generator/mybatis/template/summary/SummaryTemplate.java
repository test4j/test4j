package org.test4j.generator.mybatis.template.summary;

import lombok.Data;
import org.test4j.generator.mybatis.config.impl.GlobalConfig;

/**
 * 汇总类模板生成
 *
 * @author wudarui
 */
@Data
public class SummaryTemplate {
    private GlobalConfig globalConfig;

    private String templateId;

    private String fileName;

    public SummaryTemplate(String templateId, String fileName) {
        this.templateId = templateId;
        this.fileName = fileName;
    }

    public String getFilePath() {
        return String.format("%s/%s/%s",
            globalConfig.getTestOutputDir(),
            globalConfig.getPackageDir(),
            fileName);
    }
}