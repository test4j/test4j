package org.test4j.mock;

import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.module.database.sql.DataSourceCreatorFactory;

import javax.sql.DataSource;

/**
 * @author darui.wu
 */
public class SpringMock extends MockUp<AbstractBeanFactory> {
    static boolean hasMock = false;

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
            return requiredType != null && requiredType.isAssignableFrom(DataSource.class);
        }
    }
}
