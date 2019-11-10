package org.test4j.module.database.sql;

import org.test4j.module.database.IDataSourceCreator;
import org.test4j.tools.commons.ClazzHelper;
import org.test4j.tools.commons.ConfigHelper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class DataSourceCreatorFactory {

    public static Map<String, Test4JDataSource> EXIST_DATASOURCE = new HashMap<>();

    public static boolean isDataSource(String beanName) {
        return EXIST_DATASOURCE.containsKey(beanName);
    }

    public static DataSource create(String dataSourceName) {
        if (EXIST_DATASOURCE.containsKey(dataSourceName)) {
            return EXIST_DATASOURCE.get(dataSourceName);
        } else {
            Test4JDataSource dataSource = createDataSource(dataSourceName);
            EXIST_DATASOURCE.put(dataSourceName, dataSource);

            DataSourceScriptHelper.runInitScripts(dataSource, dataSourceName);
            return dataSource;
        }
    }

    public static Test4JDataSource createDataSource(String dataSourceName) {
        String factory = ConfigHelper.getDataSourceKey(dataSourceName, "create.factory");
        IDataSourceCreator dataSourceFactory = new DataSourceDefaultCreator();
        if (factory != null && !"".equals(factory.trim())) {
            dataSourceFactory = (IDataSourceCreator) ClazzHelper.createInstanceOfType(factory);
        }
        DataSource dataSource = dataSourceFactory.createDataSource(dataSourceName);
        return dataSource instanceof Test4JDataSource ?
                (Test4JDataSource) dataSource : new Test4JDataSource(dataSourceName, dataSource);
    }
}
