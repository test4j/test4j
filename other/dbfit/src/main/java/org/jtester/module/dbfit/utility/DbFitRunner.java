package org.jtester.module.dbfit.utility;

import java.util.Map;

import org.jtester.module.dbfit.exception.DbFitException;
import org.jtester.module.dbfit.fixture.DatabaseFixture;

@SuppressWarnings({ "rawtypes" })
public class DbFitRunner extends FitRunner {
	public DbFitRunner() {
		super();
	}

	public DbFitRunner(String dbfitDir) {
		super(dbfitDir);
	}

	/**
	 * 运行dbfit文件
	 * 
	 * @param claz
	 * @param url
	 * @throws Exception
	 */
	public void runDbFitTest(Class claz, final String url) {
		try {
			this.runFitTest(claz, url);
		} catch (Throwable e) {
			throw new DbFitException("run wiki[" + url + "] error.", e);
		}
	}

	@Override
	protected String decoratedWiki(String wiki) {
		if (wiki.contains(DatabaseFixture.class.getName())) {
			return wiki;
		} else {
			StringBuffer buffer = new StringBuffer();
			buffer.append(String.format("|!-%s-!|", DatabaseFixture.class.getName()));
			buffer.append("\n");
			buffer.append(wiki);
			return buffer.toString();
		}
	}

	private static DbFitRunner defaultRunner = new DbFitRunner();

	/**
	 * 通过程序来准备数据库数据<br>
	 * 
	 * @param clazz
	 * @param wiki
	 * @param wikis
	 */
	public static void runDbFit(Class clazz, String wiki, String... wikis) {
		defaultRunner.runDbFitTest(clazz, wiki);
		for (String curr : wikis) {
			defaultRunner.runDbFitTest(clazz, curr);
		}
	}

	public static void runDbFit(Class clazz, boolean cleanSymbols, String wiki, String... wikis) {
		runDbFit(clazz, wiki, wikis);
		if (cleanSymbols) {
			SymbolUtil.cleanSymbols();
		}
	}

	/**
	 * 通过程序来准备数据库数据<br>
	 * 同时运行完毕，不清空变量
	 * 
	 * @param clazz
	 * @param symbols
	 *            wiki变量
	 * @param wiki
	 * @param wikis
	 */
	public static void runDbFit(Class clazz, Map<String, ?> symbols, String wiki, String... wikis) {
		SymbolUtil.setSymbol(symbols);
		runDbFit(clazz, wiki, wikis);
	}

	/**
	 * 通过程序来准备数据库数据<br>
	 * 
	 * @param clazz
	 * @param symbols
	 * @param cleanSymbols
	 * @param wiki
	 * @param wikis
	 */
	public static void runDbFit(Class clazz, Map<String, Object> symbols, boolean cleanSymbols, String wiki,
			String... wikis) {
		runDbFit(clazz, symbols, wiki, wikis);
		if (cleanSymbols) {
			SymbolUtil.cleanSymbols();
		}
	}
}
