package org.test4j.mock;

import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.module.database.sql.DataSourceCreatorFactory;
import org.test4j.tools.commons.ConfigHelper;

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
        if (this.isDataSource(name, requiredType)) {
            MessageHelper.info("===========AbstractBeanFactoryMock===========");
            return (T) DataSourceCreatorFactory.create(name);
        } else {
            return it.proceed(name, requiredType, args, typeCheckOnly);
        }
    }

    private <T> boolean isDataSource(String name, Class<T> requiredType) {
        if (DataSourceCreatorFactory.isDataSource(name)) {
            return true;
        } else {
            return ConfigHelper.getDataSourceList().contains(name);
            //return requiredType != null && requiredType.isAssignableFrom(DataSource.class);
        }
    }
}
