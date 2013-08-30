package org.jtester.module.database.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ TYPE, METHOD })
@Retention(RUNTIME)
@Inherited
public @interface Transactional {

	TransactionMode value() default TransactionMode.DEFAULT;

	/**
	 * 有多个事务管理时，指定事务管理bena的名称
	 * 
	 * @return
	 */
	String transactionManagerName() default "";

	/**
	 * Defining the available transaction modes for a test. Defines whether a
	 * test must be run in a transaction and, if yes, what is the
	 * commit/rollback behavior.
	 * 
	 */
	public static enum TransactionMode {

		/**
		 * Value indicating that transactions should be disabled, i.e. the test
		 * should not be run in a transaction
		 */
		DISABLED,

		/**
		 * Value indicating that the test should be executed in a transaction,
		 * and that this transaction must be committed at the end of the test.
		 */
		COMMIT,

		/**
		 * Value indicating that the test should be executed in a transaction,
		 * and that this transaction must be rollbacked at the end of the test.
		 */
		ROLLBACK,

		/**
		 * Value indicating that the default behavior is defined by the jtester
		 * property <code>DatabaseModule.Transactional.value.default</code> is
		 * in use.
		 */
		DEFAULT;
	}
}
