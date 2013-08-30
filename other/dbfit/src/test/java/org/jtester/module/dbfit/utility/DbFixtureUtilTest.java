package org.jtester.module.dbfit.utility;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jtester.module.dbfit.IDbFit;
import org.jtester.module.dbfit.annotations.DbFit;
import org.jtester.module.dbfit.utility.DBFitSqlRunner;
import org.jtester.testng.JTester;
import org.jtester.tools.commons.ExceptionWrapper;
import org.jtester.tools.commons.ResourceHelper;
import org.testng.annotations.Test;

@Test(groups = "jtester")
public class DbFixtureUtilTest extends JTester implements IDbFit {

	@Test
	@DbFit(when = "testExecuteFromFile.when.wiki", then = "testExecuteFromFile.then.wiki")
	public void testExecuteFromFile() throws Exception {
		DBFitSqlRunner.instance.executeFromFile("org/jtester/module/dbfit/utility/executeFile.sql");
	}

	@DbFit(when = "testTransaction.when.wiki", then = "testTransaction.then.wiki")
	public void testTransaction() {

	}

	@Test(description = "验证变量回显功能_包含匹配和不匹配的情况")
	@DbFit(when = "testExecuteFromFile.when.wiki")
	public void testRunDbFit_VerifyVarable() throws Exception {
		DBFitSqlRunner.instance.executeFromFile("org/jtester/module/dbfit/utility/executeFile.sql");
		fit.setSymbol("first_name1", "dddd1");
		fit.setSymbol("first_name2", "eeee");

		try {
			fit.runDbFit(this.getClass(), "test_var_show.then.wiki");
			want.fail();
		} catch (Throwable e) {
			String file = findHtmlFileFromException(e);
			String html = ResourceHelper.readFromFile(new File(file));
			want.string(html).contains("= eeee").contains("= dddd1");
		}
	}

	private String findHtmlFileFromException(Throwable e) {
		String msg = ExceptionWrapper.toString(e);

		Pattern p = Pattern.compile("file:\\/\\/(.*)\\.html");
		Matcher m = p.matcher(msg);
		if (m.find()) {
			String file = m.group(1);
			return file + ".html";
		}
		e.printStackTrace();
		throw new RuntimeException("find result html file error.");
	}

	@Test(description = "变量是非字符串的_在query语句中使用")
	@DbFit(when = "use_symbol_not_string.when.wiki", then = "use_symbol_not_string.then.wiki")
	public void testSetSymbo() {
		fit.setSymbol("limited", 2);
	}

	@DbFit(when = "use_symbol_not_string_oracle.when.wiki", then = "use_symbol_not_string_oracle.then.wiki")
	@Test(groups = { "broken-install", "oracle" }, description = "变量是非字符串的_在query语句中使用_oracle环境")
	public void testSetSymbol_Oracle() {
		fit.setSymbol("limitrow", 2);
	}
}
