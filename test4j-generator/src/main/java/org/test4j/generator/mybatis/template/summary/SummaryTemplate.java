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

    public static List<SummaryTemplate> summaries = new ArrayList<>();

    static {
        summaries.add(new SummaryTemplate("templates/mix/Mixes.java.vm", "TableMixes.java"));
        summaries.add(new SummaryTemplate("templates/ITable.java.vm", "ITable.java"));
        summaries.add(new SummaryTemplate("templates/DataSourceScript.java.vm", "DataSourceScript.java"));
        summaries.add(new SummaryTemplate("templates/datamap/TM.java.vm", "datamap/TM.java"));
        summaries.add(new SummaryTemplate("templates/datamap/EM.java.vm", "datamap/EM.java"));
    }
}
