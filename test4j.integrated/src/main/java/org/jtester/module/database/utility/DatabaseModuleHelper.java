package org.jtester.module.database.utility;

import org.jtester.module.core.utility.ModulesManager;
import org.jtester.module.database.DatabaseModule;

/**
 * Class providing access to the functionality of the database module using
 * static methods. Meant to be used directly in unit tests.
 */
public class DatabaseModuleHelper {

	/**
	 * Gets the instance DatabaseModule that is registered in the modules
	 * repository. This instance implements the actual behavior of the static
	 * methods in this class. This way, other implementations can be plugged in,
	 * while keeping the simplicity of using static methods.
	 * 
	 * @return the instance, not null
	 */
	public static DatabaseModule getDatabaseModule() {
		return ModulesManager.getModuleInstance(DatabaseModule.class);
	}

//	/**
//	 * 当前testcase中测试是否被disabled
//	 * 
//	 * @param testObject
//	 * @param testMethod
//	 * @return
//	 */
//	public static boolean isDisabledTransaction(Object testObject, Method testMethod) {
//		boolean isEnabledDatabaseModule = ModulesManager.isModuleEnabled(DatabaseModule.class);
//		if (isEnabledDatabaseModule) {
//			boolean enabled = TestContext.isTransactionsEnabled();
//			return !enabled;
//		} else {
//			return true;
//		}
//	}
}
