package org.test4j.generator.mybatis.template;

import org.test4j.generator.mybatis.template.datamap.EntityMapTemplate;
import org.test4j.generator.mybatis.template.datamap.TableMapTemplate;
import org.test4j.generator.mybatis.template.mix.TableMixTemplate;
import org.test4j.generator.mybatis.template.summary.SummaryTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * DataMapTemplateList
 *
 * @author wudarui
 */
public interface DataMapTemplateList {
    List<BaseTemplate> datamap_list = Arrays.asList(
        new EntityMapTemplate(),
        new TableMapTemplate(),
        new TableMixTemplate()
    );

    List<SummaryTemplate> summaries = Arrays.asList(
        new SummaryTemplate("templates/mix/Mixes.java.vm", "TableMixes.java"),
        new SummaryTemplate("templates/ITable.java.vm", "ITable.java"),
        new SummaryTemplate("templates/DataSourceScript.java.vm", "DataSourceScript.java"),
        new SummaryTemplate("templates/datamap/TM.java.vm", "datamap/TM.java"),
        new SummaryTemplate("templates/datamap/EM.java.vm", "datamap/EM.java")
    );
}