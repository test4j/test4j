package org.test4j;

import org.test4j.module.database.sql.DataSourceCreatorFactory;
import org.test4j.tools.commons.ListHelper;

import javax.sql.DataSource;
import java.util.List;

/**
 * 一些测试中常用到的方法快捷入口
 */
public interface IUtil {
    /**
     * 创建test4j数据源
     *
     * @param dataSource
     * @return
     */
    default DataSource createDataSource(String dataSource) {
        return DataSourceCreatorFactory.createDataSource(dataSource);
    }

    /**
     * 构造列表
     *
     * @param items
     * @return
     */
    default List list(Object... items) {
        return ListHelper.toList(items);
    }
}