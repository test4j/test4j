package org.test4j.module.dbfit.utility;

import org.test4j.module.dbfit.annotations.DbFit;
import org.test4j.module.dbfit.annotations.DbFit.AUTO;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

public class ResourceHelperTest extends Test4J {

	@Test
	@DbFit(when = { "dbfit/jar/file/test.wiki" }, auto = AUTO.AUTO)
	public void testUseJarWiki() {

	}
}
