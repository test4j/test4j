package org.test4j.module.core.internal;

/**
 * test4j配置常量属性
 *
 * @author darui.wudr
 */
public interface IPropItem {
    /**
     * test4j模块加载序列<br>
     * Property that contains the names of the modules that are to be loaded.
     */
    String PROPKEY_MODULES = "test4j.modules";

    String LOG4J_XML_FILE = "log4j.xml.file";

    String DB_DATASOURCE_DEFAULT = "dataSource";

    String DB_DATASOURCE_DEFAULT_NAME = "db.dataSource.default.name";

    String CONNECT_ONLY_TESTDB = "database.only.testdb.allowing";

    String DB_DATASOURCE_LIST = "db.dataSource.list";

    String PROP_KEY_DATASOURCE_TYPE = "type";

    String PROP_KEY_DATASOURCE_URL = "url";

    String PROP_KEY_DATASOURCE_SCHEMA = "schemaName";

    String PROP_KEY_DATASOURCE_USERNAME = "userName";

    String PROP_KEY_DATASOURCE_PASSWORD = "password";

    String PROP_KEY_DATASOURCE_DRIVER = "driver";
}
