package org.test4j.module.spring.strategy;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.test4j.module.database.environment.DBEnvironmentFactory;
import org.test4j.tools.commons.ConfigHelper;

@SuppressWarnings("rawtypes")
public class Test4JBeanFactory extends DefaultListableBeanFactory {

    public Test4JBeanFactory(final BeanFactory parentBeanFactory) {
        super(parentBeanFactory);
    }

    @Override
    public Object getBean(final String name, final Class requiredType, final Object[] args) throws BeansException {
        Object bean = getMyBean(name, requiredType, args);
        return bean;
    }

    /**
     * 返回容器中的spring对象
     * 
     * @param name
     * @return
     */
    public Object getSpringBean(final String name) {
        Object bean = this.getBean(name, null, null);
        return bean;
    }

    /**
     * 返回依赖项的代理对象（对象的调用转向spring容器中的spring bean）<br>
     * <br>
     * {@inheritDoc}
     * 
     * @param name
     * @return
     */
    @Override
    public Object getBean(final String name) {
        return this.getProxyBean(name);
    }

    /**
     * 返回依赖项的代理对象（对象的调用转向spring容器中的spring bean）
     * 
     * @param name
     * @return
     */
    public Object getProxyBean(final String name) {
        return this.getBean(name, null, null);
    }

    private Object getMyBean(final String name, final Class requiredType, final Object[] args) throws BeansException {
        if (ConfigHelper.isSpringDataSourceName(name)) {
            DataSource dataSource = DBEnvironmentFactory.getDefaultDBEnvironment()
                    .getDataSourceAndActivateTransactionIfNeeded();
            return dataSource;
        } else {
            Object bean = super.getBean(name, requiredType, args);
            return bean;
        }
    }
}
