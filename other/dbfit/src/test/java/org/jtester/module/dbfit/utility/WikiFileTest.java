package org.jtester.module.dbfit.utility;

import org.jtester.module.dbfit.utility.WikiFile;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = "jtester")
public class WikiFileTest extends JTester {

	@Test
	public void testFindWikiFile() throws Exception {
		WikiFile wiki = WikiFile.findWikiFile(null, "dbfit/jar/file/test.wiki");
		want.object(wiki).propertyEq("wikiUrl", "dbfit/jar/file/test.wiki").propertyEq("wikiName", "test");
	}

	@Test
	public void testReadWikiContent() throws Exception {
		WikiFile wiki = WikiFile.findWikiFile(null, "dbfit/jar/file/test.wiki");
		String content = wiki.readWikiContent();
		want.string(content).notNull().contains("|connect|");
	}

}
