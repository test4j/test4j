package org.jtester.module.database.transaction;

import org.springframework.transaction.PlatformTransactionManager;

/**
 * 事务配置
 * 
 * @author darui.wudr
 * 
 */
public interface TransactionManagementConfiguration {
	/**
	 * @param testObject
	 *            The test object, not null
	 * @return True if this implementation is able to supply a suitable
	 *         <code>PlatformTransactionManager</code> for the given test object
	 */
	boolean isApplicableFor(Object testObject);

	/**
	 * Returns a <code>PlatformTransactionManager</code> that can provide
	 * transactional behavior for the given test object. May only be invoked if
	 * {@link #isApplicableFor(Object)} returns true for this test object.
	 * 
	 * @param testObject
	 *            The test object, not null
	 * @return A <code>PlatformTransactionManager</code> that can provide
	 *         transactional behavior for the given test object.
	 */
	PlatformTransactionManager getSpringPlatformTransactionManager(Object testObject);

	boolean isTransactionalResourceAvailable(Object testObject);

	Integer getPreference();
}
