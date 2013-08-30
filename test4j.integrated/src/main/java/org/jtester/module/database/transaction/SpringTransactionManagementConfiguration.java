package org.jtester.module.database.transaction;

import java.lang.reflect.Method;
import java.util.Map;

import org.jtester.module.JTesterException;
import org.jtester.module.core.TestContext;
import org.jtester.module.database.annotations.Transactional;
import org.jtester.module.spring.SpringTestedContext;
import org.jtester.tools.commons.AnnotationHelper;
import org.jtester.tools.commons.ClazzHelper;
import org.jtester.tools.commons.StringHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;

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
			throw new JTesterException("Could not find a bean of type "
					+ platformTransactionManagerClass.getSimpleName()
					+ " in the spring ApplicationContext for this class");
		}
		if (platformTransactionManagers.size() > 1) {
			Method testMethod = TestContext.currTestedMethod();
			String transactionManagerName = AnnotationHelper.getMethodOrClassLevelAnnotationProperty(
					Transactional.class, "transactionManagerName", "", testMethod, testObject.getClass());
			if (StringHelper.isBlankOrNull(transactionManagerName))
				throw new JTesterException(
						"Found more than one bean of type "
								+ platformTransactionManagerClass.getSimpleName()
								+ " in the spring ApplicationContext for this class. Use the transactionManagerName on the @Transactional"
								+ " annotation to select the correct one.");
			if (!platformTransactionManagers.containsKey(transactionManagerName))
				throw new JTesterException("No bean of type " + platformTransactionManagerClass.getSimpleName()
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
