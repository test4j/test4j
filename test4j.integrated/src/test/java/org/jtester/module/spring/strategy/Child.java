package org.jtester.module.spring.strategy;

import java.util.HashMap;
import java.util.Map;

import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringBeanFrom;
import org.test4j.fortest.service.BeanClazzUserServiceImpl;
import org.test4j.fortest.service.UserService;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
public abstract class Child extends Parent {
	@SpringBeanByName(claz = BeanClazzUserServiceImpl.class)
	protected UserService userService;
	@SpringBeanFrom
	protected Map map = new HashMap() {
		{
			this.put("mark", "spring init");
		}
	};
}