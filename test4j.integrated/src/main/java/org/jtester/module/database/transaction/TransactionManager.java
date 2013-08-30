package org.jtester.module.database.transaction;

import java.util.Set;

import javax.sql.DataSource;

/**
 * 测试事务管理
 * 
 * @author darui.wudr
 * 
 */
public interface TransactionManager {
	/**
	 * Initialize the transaction manager
	 * 
	 * @param transactionManagementConfigurations
	 *            Set of possible providers of a spring
	 *            <code>PlatformTransactionManager</code>, not null
	 */
	void init(Set<TransactionManagementConfiguration> transactionManagementConfigurations);

	/**
	 * Wraps the given <code>DataSource</code> in a transactional proxy.
	 * <p/>
	 * The <code>DataSource</code> returned will make sure that, for the
	 * duration of a transaction, the same <code>java.sql.Connection</code> is
	 * returned, and that invocations on the close() method of these connections
	 * are suppressed.
	 * 
	 * @param dataSource
	 *            The data source to wrap, not null
	 * @return A transactional data source, not null
	 */
	DataSource getTransactionalDataSource(DataSource dataSource);

	/**
	 * Starts a transaction.
	 */
	void startTransaction();

	/**
	 * Commits the currently active transaction. This transaction must have been
	 * initiated by calling {@link #startTransaction(Object)} with the same
	 * testObject within the same thread.
	 */
	void commit();

	/**
	 * Rolls back the currently active transaction. This transaction must have
	 * been initiated by calling {@link #startTransaction(Object)} with the same
	 * testObject within the same thread.
	 */
	void rollback();

	void activateTransactionIfNeeded();
}
