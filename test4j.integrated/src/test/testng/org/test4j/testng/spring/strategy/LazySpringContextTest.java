package org.test4j.testng.spring.strategy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.test4j.fortest.beans.User;
import org.test4j.fortest.service.BeanClazzUserServiceImpl;
import org.test4j.fortest.service.UserDao;
import org.test4j.fortest.service.UserServiceImpl;
import org.test4j.module.database.IDatabase;
import org.test4j.module.spring.ISpring;
import org.test4j.module.spring.annotations.SpringInitMethod;
import org.test4j.module.spring.strategy.Child;
import org.test4j.module.spring.strategy.JTesterSpringContext;
import org.test4j.module.spring.strategy.LazySpringContext;
import org.test4j.module.spring.strategy.Parent;
import org.test4j.module.spring.utility.SpringModuleHelper;
import org.testng.annotations.Test;

@SuppressWarnings({ "rawtypes" })
@Test(groups = { "all-test", "spring" })
public class LazySpringContextTest extends Child implements IDatabase, ISpring {
	Map<Class, JTesterSpringContext> SHARED_SPRING_CONTEXT = reflector.getStaticField(SpringModuleHelper.class,
			"SHARED_SPRING_CONTEXT");

	/**
	 * 容器启动前，共享的spring容器应该不存在
	 */
	@SpringInitMethod
	protected void checkSharedSpring() {
		JTesterSpringContext context = SHARED_SPRING_CONTEXT.get(Parent.class);
		want.object(context).isNull();
	}

	/**
	 * 共享sping容器只加载定义Annotation类或其父类的@SpringBeanFrom，其子类的应该被忽略
	 */
	@Test
	public void testGetTestedClazzz() {
		LazySpringContext context = reflector.newInstance(LazySpringContext.class);
		Class claz = reflector.invoke(context, "getTestedClazzz");
		want.string(claz.getName()).isEqualTo(Parent.class.getName());
	}

	/**
	 * 共享sping容器只加载定义Annotation类或其父类的@SpringBeanFrom，其子类的应该被忽略
	 */
	@Test
	public void testSpringBeanFrom_DefineInChild() {
		try {
			spring.getBean("map");
			want.fail();
		} catch (NoSuchBeanDefinitionException e) {
			String msg = e.getMessage();
			want.string(msg).contains("No bean named 'map' is defined");
		}
	}

	/**
	 * 共享spring容器模式下，定义在Annotation声明类中的@SpringBeanFrom应该被加载
	 */
	@Test
	public void testLazySpringContext_SpringBeanFrom() {
		UserDao bean = spring.getBean("userDao");
		List<User> users = bean.findAllUser();
		want.list(users).sizeEq(1);
	}

	/**
	 * 共享容器中@SpringBeanByName的claz属性被无视,动态加载无效
	 */
	@Test
	public void testLazySpringContext_SpringBeanByName() {
		want.object(this.userService).clazIs(UserServiceImpl.class);
		try {
			want.object(this.userService).clazIs(BeanClazzUserServiceImpl.class);
			want.fail();
		} catch (AssertionError e) {
		}
	}

	/**
	 * 容器启动后，共享的spring容器应该被存在SpringModuleHelper变量SHARED_SPRING_CONTEXT中
	 */
	@Test
	public void testShareSpring() {
		JTesterSpringContext context = SHARED_SPRING_CONTEXT.get(Parent.class);
		want.object(context).notNull();
	}
}
