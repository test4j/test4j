package org.jtester.module.dbfit;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.jtester.module.core.Module;
import org.jtester.module.core.TestListener;
import org.jtester.module.core.utility.MessageHelper;
import org.jtester.module.database.environment.DBEnvironment;
import org.jtester.module.database.environment.DBEnvironmentFactory;
import org.jtester.module.dbfit.DbFitTestedContext.RunIn;
import org.jtester.module.dbfit.annotations.DbFit;
import org.jtester.module.dbfit.annotations.FitVar;
import org.jtester.module.dbfit.utility.AutoFindDbFit;
import org.jtester.module.dbfit.utility.DBFitSqlRunner;
import org.jtester.module.dbfit.utility.DbFitRunner;
import org.jtester.module.dbfit.utility.SymbolUtil;
import org.jtester.module.tracer.TracerManager;
import org.jtester.tools.commons.AnnotationHelper;

@SuppressWarnings("rawtypes")
public class DbFitModule implements Module {

	public void init() {
		SymbolUtil.init();// initial SymbolUtil
	}

	public void afterInit() {
		;
	}

	public static void setSymbols(DbFit dbFit) {
		if (dbFit == null) {
			return;
		}
		Map<String, Object> symbols = exactFitVars(dbFit);
		SymbolUtil.setSymbol(symbols);
	}

	/**
	 * 获取DbFit中设置的参数列表
	 * 
	 * @param dbFit
	 * @return
	 */
	private static Map<String, Object> exactFitVars(DbFit dbFit) {
		Map<String, Object> symbols = new HashMap<String, Object>();
		if (dbFit == null) {
			return symbols;
		}
		FitVar[] vars = dbFit.vars();
		if (vars == null) {
			return symbols;
		}
		for (FitVar var : vars) {
			symbols.put(var.key(), var.value());
		}
		return symbols;
	}

	/**
	 * 运行数据准备(验证)文件<br>
	 * 可以是wiki文件，sql文件。<br>
	 * 未来计划添加其他格式的文件，比如Excel, csv, xml等
	 * 
	 * @param testClazz
	 * @param files
	 * @throws SQLException
	 * @throws FileNotFoundException
	 */
	private static void runDbFiles(Class testClazz, String[] files) {
		if (files == null) {
			return;
		}
		DbFitTestedContext.setRunIn(RunIn.TestCase);
		for (String file : files) {
			if (file == null || "".equals(file.trim())) {
				throw new RuntimeException("@DbFit file name can't be null or emptry.");
			}
			if (file.endsWith(".wiki")) {
				DbFitRunner.runDbFit(testClazz, file);
			} else if (file.endsWith(".sql")) {
				DBFitSqlRunner.instance.executeFromFile(testClazz, file);
				MessageHelper.info(String.format("execute sql file[%s] successfully.", file));
			} else {
				String error = String.format(
						"@DbFit only support wiki file or sql file, please check the file[%s] format.", file);
				throw new RuntimeException(error);
			}
		}
	}

	public TestListener getTestListener() {
		return new DbFitTestListener();
	}

	protected class DbFitTestListener extends TestListener {
		@Override
		public void beforeClass(Class testClazz) {
			DbFit dbFit = AnnotationHelper.getClassLevelAnnotation(DbFit.class, testClazz);
			if (dbFit == null) {
				return;
			}
			TracerManager.suspendJDBC();// 不记录dbfit的sql语句

			DBEnvironmentFactory.changeDBEnvironment(dbFit.dataSource());
			DbFitModule.setSymbols(dbFit);
			String[] files = AutoFindDbFit.autoFindClassWhen(testClazz);
			runDbFiles(testClazz, files);
			DBEnvironmentFactory.getCurrentDBEnvironment().commit();

			TracerManager.continueJDBC();
		}

		/**
		 * dbfit放在beforeMethodRunning中初始化，而不是放在setupMthod中初始化的原因是：<br>
		 * setupMthod中的异常会导致后面所有的测试方法broken <br>
		 * <br>
		 * {@inheritDoc}
		 */
		@Override
		public void beforeRunning(Object testObject, Method testMethod) {
			Class testedClazz = testObject.getClass();
			TracerManager.suspendJDBC();// 不记录dbfit的sql语句

			DbFit dbFit = testMethod.getAnnotation(DbFit.class);
			if (dbFit != null) {
				DBEnvironmentFactory.changeDBEnvironment(dbFit.dataSource());
				DbFitModule.setSymbols(dbFit);
			}
			String[] files = AutoFindDbFit.autoFindMethodWhen(testedClazz, testMethod);
			runDbFiles(testedClazz, files);

			TracerManager.continueJDBC();
		}

		@Override
		public void afterRunned(Object testObject, Method testMethod, Throwable testThrowable) {
			Class testedClazz = testObject.getClass();
			TracerManager.suspendJDBC();

			DbFit dbFit = testMethod.getAnnotation(DbFit.class);
			if (dbFit != null) {
				DBEnvironmentFactory.changeDBEnvironment(dbFit.dataSource());
				DbFitModule.setSymbols(dbFit);
			}
			String[] files = AutoFindDbFit.autoFindMethodThen(testedClazz, testMethod);
			runDbFiles(testedClazz, files);
			SymbolUtil.cleanSymbols();

			DBEnvironmentFactory.changeDBEnvironment(DBEnvironment.DEFAULT_DATASOURCE_NAME);
			TracerManager.continueJDBC();
		}

		@Override
		protected String getName() {
			return "DbFitTestListener";
		}
	}
}
