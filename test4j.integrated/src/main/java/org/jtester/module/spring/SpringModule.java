package org.jtester.module.spring;

import java.lang.reflect.Method;

import org.jtester.module.core.Module;
import org.jtester.module.core.TestContext;
import org.jtester.module.core.TestListener;
import org.jtester.module.core.utility.ModulesManager;
import org.jtester.module.database.DatabaseModule;
import org.jtester.module.database.environment.DBEnvironmentFactory;
import org.jtester.module.database.transaction.SpringTransactionManagementConfiguration;
import org.jtester.module.spring.strategy.ApplicationContextFactory;
import org.jtester.module.spring.strategy.JTesterBeanFactory;
import org.jtester.module.spring.strategy.JTesterSpringContext;
import org.jtester.module.spring.strategy.injector.SpringBeanInjector;
import org.jtester.module.spring.utility.SpringInitInvoker;
import org.jtester.module.spring.utility.SpringModuleHelper;
import org.jtester.tools.commons.ConfigHelper;

@SuppressWarnings("rawtypes")
public class SpringModule implements Module {
	private ApplicationContextFactory contextFactory;

	/**
	 * 根据配置初始化ApplicationContextFactory <br>
	 * <br>
	 * {@inheritDoc}
	 */
	public void init() {
		contextFactory = ConfigHelper.getInstance(SPRING_APPLICATION_CONTEXT_FACTORY_CLASS_NAME);
		SpringModuleHelper.mockCglibAopProxy();
	}

	public void afterInit() {
		boolean isDatabaseModuleEnabled = ModulesManager.isModuleEnabled(DatabaseModule.class);
		if (isDatabaseModuleEnabled) {
			DBEnvironmentFactory.getDefaultDBEnvironment().registerTransactionManagementConfiguration(
					new SpringTransactionManagementConfiguration());
		}
	}

	/**
	 * 强制让SpringApplicationContext失效，重新初始化
	 * 
	 * @param testedObject
	 */
	public void invalidateApplicationContext() {
		Class testClazz = TestContext.currTestedClazz();
		SpringTestedContext.removeSpringContext();
		JTesterSpringContext springContext = SpringModuleHelper.initSpringContext(testClazz, this.contextFactory);
		SpringTestedContext.setSpringContext(springContext);
	}

	public TestListener getTestListener() {
		return new SpringTestListener();
	}

	/**
	 * The {@link TestListener} for this module
	 */
	protected class SpringTestListener extends TestListener {
		@Override
		public void beforeClass(Class testClazz) {
			SpringModuleHelper.resetDumpReference();

			SpringInitInvoker.invokeSpringInitMethod(TestContext.currTestedObject());

			JTesterSpringContext springContext = SpringModuleHelper.initSpringContext(testClazz, contextFactory);
			SpringTestedContext.setSpringContext(springContext);
		}

		/**
		 * 重新注入spring bean,避免字段的值受上个测试的影响<br>
		 * <br>
		 * {@inheritDoc}
		 */
		@Override
		public void beforeMethod(Object testObject, Method testMethod) {
			JTesterBeanFactory beanFactory = (JTesterBeanFactory) SpringTestedContext.getSpringBeanFactory();
			if (beanFactory != null) {
				SpringBeanInjector.injectSpringBeans(beanFactory, testObject);
			}
		}

		@Override
		public void afterClass(Object testedObject) {
			SpringTestedContext.removeSpringContext();
		}

		@Override
		protected String getName() {
			return "SpringTestListener";
		}
	}
}
