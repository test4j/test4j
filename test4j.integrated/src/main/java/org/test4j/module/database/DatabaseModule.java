package org.test4j.module.database;

import java.lang.reflect.Method;

import org.springframework.transaction.PlatformTransactionManager;
import org.test4j.module.core.Module;
import org.test4j.module.core.TestListener;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.module.database.environment.DBEnvironment;
import org.test4j.module.database.environment.DBEnvironmentFactory;

public class DatabaseModule implements Module {
	/**
	 * Property indicating if the database schema should be updated before
	 * performing the tests
	 */
	public static final String PROPERTY_UPDATEDATABASESCHEMA_ENABLED = "updateDataBaseSchema.enabled";

	/**
	 * Initializes this module using the given <code>Configuration</code>
	 * 
	 * @param configuration
	 *            The config, not null
	 */
	public void init() {
		MessageHelper.info("PlatformTransactionManager class init.");
		PlatformTransactionManager.class.getName();
	}

	/**
	 * Initializes the spring support object<br>
	 * <br>
	 * 判断是否需要去除数据库的外键约束和字段not null约束<br>
	 * <br>
	 * Disables all foreign key and not-null constraints on the configured
	 * schema's.
	 */
	public void afterInit() {
		DBEnvironmentFactory.getDefaultDBEnvironment().registerTransactionManagementConfiguration(null);
	}

	/**
	 * @return The {@link TestListener} associated with this module
	 */
	public TestListener getTestListener() {
		return new DatabaseTestListener();
	}

	protected static boolean hasExetedDisabled = false;

	/**
	 * The {@link TestListener} for this module
	 */
	protected class DatabaseTestListener extends TestListener {

		/**
		 * 初始化测试方法的事务<br>
		 * <br>
		 * {@inheritDoc}
		 */
		@Override
		public void beforeMethod(Object testObject, Method testMethod) {
			DBEnvironmentFactory.changeDBEnvironment(DBEnvironment.DEFAULT_DATASOURCE_NAME);
			DBEnvironmentFactory.startDBEnvironment();
		}

		/**
		 * 移除测试方法的事务<br>
		 * <br>
		 * {@inheritDoc}
		 */
		@Override
		public void afterMethod(Object testObject, Method testMethod) {
			DBEnvironmentFactory.closeDBEnvironment();
			DBEnvironmentFactory.changeDBEnvironment(DBEnvironment.DEFAULT_DATASOURCE_NAME);
		}

		@Override
		protected String getName() {
			return "DatabaseTestListener";
		}
	}
}
