package org.jtester.tools.commons;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import org.jtester.junit.JTester;
import org.jtester.junit.annotations.DataFrom;
import org.jtester.junit.annotations.Group;
import org.junit.Test;
@Group("common")
public class ResourceHelperTest extends JTester {
	@Test
	@DataFrom("dataForIsJarFile")
	public void testIsJarFile(String file, Boolean isJar) throws Exception {
		boolean result = ResourceHelper.isJarFile(new File(file));
		want.bool(result).is(isJar);
	}

	public static DataIterator dataForIsJarFile() {
		return new DataIterator() {
			{
				data("a/b/c.jar", true);
				data("a/b/c.JAR", true);
				data("a/b/c.zip", false);
				data("a/b/c.1jar", false);
				data("jar", false);
			}
		};
	}

	@Test
	public void testReadFromFile() throws FileNotFoundException {
		String wiki = ResourceHelper.readFromFile("classpath:dbfit/jar/file/test.wiki");
		want.string(wiki).contains("|connect|");
	}

	@Test
	public void testGetResourceUrl_InJar() {
		URL url = ResourceHelper.getResourceUrl("classpath:dbfit/jar/file/log4j.xml");
		want.string(url.toString()).start("jar:file").end("dbfit/jar/file/log4j.xml");
	}

	@Test
	public void testIsResourceExists_ClazzIsNull_FileInJar() {
		boolean isExists = ResourceHelper.isResourceExists(null, "dbfit/jar/file/test.wiki");
		want.bool(isExists).isEqualTo(true);
	}
}
