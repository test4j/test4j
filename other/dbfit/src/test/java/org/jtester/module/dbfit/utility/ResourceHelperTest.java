package org.jtester.module.dbfit.utility;

import org.jtester.module.dbfit.annotations.DbFit;
import org.jtester.module.dbfit.annotations.DbFit.AUTO;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

public class ResourceHelperTest extends JTester {

	@Test
	@DbFit(when = { "dbfit/jar/file/test.wiki" }, auto = AUTO.AUTO)
	public void testUseJarWiki() {

	}
}
