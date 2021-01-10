package org.test4j.mock;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.test4j.module.ConfigHelper;
import org.test4j.module.database.sql.DataSourceCreatorFactory;
import org.test4j.tools.Logger;

/**
 * @author darui.wu
 */
public class SpringMock extends MockUp<AbstractBeanFactory> {
    static boolean hasMock = false;
    /**
     * 要mock的class文件默认存在
     */
    static boolean classNotFound = false;

    public SpringMock() {
        hasMock = true;
    }

    @Mock
    public <T> T doGetBean(Invocation it,
                           String name,
                           Class<T> requiredType,
                           final Object[] args,
                           boolean typeCheckOnly)
        throws BeansException {
        if (this.isDataSource(name)) {
            Logger.info("===========AbstractBeanFactoryMock===========");
            return (T) DataSourceCreatorFactory.create(name);
        } else {
            return it.proceed(name, requiredType, args, typeCheckOnly);
        }
    }

    private <T> boolean isDataSource(String name) {
        if (DataSourceCreatorFactory.isDataSource(name)) {
            return true;
        } else {
            return ConfigHelper.getDataSourceList().contains(name);
        }
    }
}