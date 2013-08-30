package org.test4j.module.database.transaction;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.test4j.module.Test4JException;
import org.test4j.module.core.TestContext;
import org.test4j.module.database.annotations.Transactional;
import org.test4j.module.spring.SpringTestedContext;
import org.test4j.tools.commons.AnnotationHelper;
import org.test4j.tools.commons.ClazzHelper;
import org.test4j.tools.commons.StringHelper;

@SuppressWarnings("unchecked")
public class SpringTransactionManagementConfiguration implements TransactionManagementConfiguration {
	public boolean isApplicableFor(Object testObject) {
		ApplicationContext context = SpringTestedContext.getSpringContext();
		if (context == null) {
			return false;
		}
		return context.getBeansOfType(getPlatformTransactionManagerClass()).size() != 0;
	}

	public PlatformTransactionManager getSpringPlatformTransactionManager(Object testObject) {
		ApplicationContext context = SpringTestedContext.getSpringContext();
		Class<?> platformTransactionManagerClass = getPlatformTransactionManagerClass();
		Map<String, PlatformTransactionManager> platformTransactionManagers = context
				.getBeansOfType(platformTransactionManagerClass);
		if (platformTransactionManagers.size() == 0) {
			throw new Test4JException("Could not find a bean of type "
					+ platformTransactionManagerClass.getSimpleName()
					+ " in the spring ApplicationContext for this class");
		}
		if (platformTransactionManagers.size() > 1) {
			Method testMethod = TestContext.currTestedMethod();
			String transactionManagerName = AnnotationHelper.getMethodOrClassLevelAnnotationProperty(
					Transactional.class, "transactionManagerName", "", testMethod, testObject.getClass());
			if (StringHelper.isBlankOrNull(transactionManagerName))
				throw new Test4JException(
						"Found more than one bean of type "
								+ platformTransactionManagerClass.getSimpleName()
								+ " in the spring ApplicationContext for this class. Use the transactionManagerName on the @Transactional"
								+ " annotation to select the correct one.");
			if (!platformTransactionManagers.containsKey(transactionManagerName))
				throw new Test4JException("No bean of type " + platformTransactionManagerClass.getSimpleName()
						+ " found in the spring ApplicationContext with the name " + transactionManagerName);
			return platformTransactionManagers.get(transactionManagerName);
		}
		return platformTransactionManagers.values().iterator().next();
	}

	public boolean isTransactionalResourceAvailable(Object testObject) {
		return true;
	}

	public Integer getPreference() {
		return 20;
	}

	protected Class<?> getPlatformTransactionManagerClass() {
		Class<?> clazz = ClazzHelper.getClazz("org.springframework.transaction.PlatformTransactionManager");
		return clazz;
	}
}
