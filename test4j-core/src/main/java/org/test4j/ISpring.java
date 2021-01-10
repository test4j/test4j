package org.test4j;

import org.springframework.beans.factory.BeanFactory;
import org.test4j.integration.spring.SpringEnv;

public interface ISpring {
    SpringHelper spring = new SpringHelper();

    class SpringHelper {

        /**
         * 如果spring容器有效，返回名称为beanname的spring bean
         *
         * @param beanName
         * @return
         */
        public <T> T getBean(String beanName) {
            return (T) SpringEnv.getBeanByName(beanName);
        }

        /**
         * 返回当前spring容器
         *
         * @return
         */
        public BeanFactory getBeanFactory() {
            return SpringEnv.getSpringContext().orElse(null);
        }
    }
}