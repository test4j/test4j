package org.test4j.generator.template;

import org.test4j.generator.template.impl.*;
import org.test4j.generator.template.summary.SummaryTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * DataMapTemplateList
 *
 * @author wudarui
 */
public interface DataMapTemplateList {
    List<BaseTemplate> test_list = Arrays.asList(
        new DataMapTemplate(),
        new TableMixTemplate()
    );

    List<BaseTemplate> entity_list = Arrays.asList(
        new EntityTemplate(),
        new DaoIntfTemplate(),
        new DaoImplTemplate()
    );

    List<SummaryTemplate> summaries = Arrays.asList(
        new SummaryTemplate("templates/summary/Mixes.java.vm", "TableMixes.java"),
        new SummaryTemplate("templates/summary/ITable.java.vm", "ITable.java"),
        new SummaryTemplate("templates/summary/DataSourceScript.java.vm", "DataSourceScript.java"),
        new SummaryTemplate("templates/summary/DM.java.vm", "DM.java")
    );
}