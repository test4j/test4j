package org.test4j.module.spring.mock;

import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.test4j.junit.DataFrom;
import org.test4j.module.database.sql.Test4JDataSource;

import javax.sql.DataSource;

public class AbstractBeanFactoryMock extends MockUp<AbstractBeanFactory> {
    @Mock
    public <T> T doGetBean(Invocation it,
                           String name, Class<T> requiredType, final Object[] args, boolean typeCheckOnly)
            throws BeansException {
        Object bean = it.proceed(name, requiredType, args, typeCheckOnly);
        if (bean instanceof DataFrom.DataSource) {
            return (T) Test4JDataSource.wrapperWithTest4JDataSource(name, (DataSource) bean);
        } else {
            return (T) bean;
        }
    }
}
