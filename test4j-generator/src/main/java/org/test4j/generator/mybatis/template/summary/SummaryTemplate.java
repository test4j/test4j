package org.test4j.generator.mybatis.template.summary;

import lombok.Data;
import org.test4j.generator.mybatis.model.Generator;

import java.util.ArrayList;
import java.util.List;

@Data
public class SummaryTemplate {
    private Generator generator;

    private String templateId;

    private String fileName;

    public SummaryTemplate(String templateId, String fileName) {
        this.templateId = templateId;
        this.fileName = fileName;
    }

    public String getFilePath() {
        return String.format("%s/%s/%s",
            generator.getTestOutputDir(),
            generator.getPackageDir(),
            fileName);
    }


}
