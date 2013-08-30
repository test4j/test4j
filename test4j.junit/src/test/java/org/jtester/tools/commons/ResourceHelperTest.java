package org.jtester.tools.commons;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import org.jtester.hamcrest.matcher.string.StringMode;
import org.jtester.module.ICore;
import org.junit.Test;

public class ResourceHelperTest implements ICore {

	private final static String codedir = System.getProperty("user.dir") + "/../jtester.core/src/main/java";

	@Test
	public void findWikiFile_FilePath() throws Exception {
		File file = ResourceHelper.findWikiFile(null,
				String.format("file:%s/org/jtester/tools/commons/ResourceHelper.java", codedir));
		want.file(file).isExists();
	}

	@Test
	public void findWikiFile_ClassPath() throws Exception {
		InputStream stream = ResourceHelper.getResourceAsStream("org/jtester/tools/commons/ResourceHelperTest.class");
		want.object(stream).notNull();
		File file = ResourceHelper.findWikiFile(null, "org/jtester/tools/commons/ResourceHelperTest.class");
		want.file(file).isExists();
	}

	@Test
	public void findWikiFile_ClassPath2() throws Exception {
		File file = ResourceHelper.findWikiFile(ResourceHelperTest.class, "ResourceHelperTest.class");
		want.file(file).isExists();
	}

	@Test
	public void getFileEncodingCharset_utf() {
		File utf8 = new File("src/test/resources/org/jtester/tools/commons/is_utf8.txt");
		String encoding = ResourceHelper.getFileEncodingCharset(utf8);
		want.string(encoding).isEqualTo("UTF-8", StringMode.IgnoreCase);
	}

	@Test
	public void getFileEncodingCharset_gbk() {
		File gbk = new File("src/test/resources/org/jtester/tools/commons/is_gbk.txt");
		String encoding = ResourceHelper.getFileEncodingCharset(gbk);
		want.string(encoding).isEqualTo("GB2312");
	}

	@Test
	public void testGetResourceAsStream_file() throws FileNotFoundException {
		InputStream is = ResourceHelper
				.getResourceAsStream("file:src/test/resources/org/jtester/tools/commons/executeFile.sql");
		want.object(is).notNull();
		String sql = ResourceHelper.convertStreamToSQL(is);
		want.string(sql).contains("insert into tdd_user").notContain("--").notContain("#");
	}

	@Test
	public void testGetResourceAsStream_classpath() throws FileNotFoundException {
		InputStream is = ResourceHelper.getResourceAsStream("classpath:org/jtester/tools/commons/executeFile.sql");
		want.object(is).notNull();
		String sql = ResourceHelper.convertStreamToSQL(is);
		want.string(sql).contains("insert into tdd_user").notContain("--").notContain("#");
	}

	@Test
	public void testGetResourceAsStream_classpath2() throws FileNotFoundException {
		InputStream is = ResourceHelper.getResourceAsStream("org/jtester/tools/commons/executeFile.sql");
		want.object(is).notNull();
		String sql = ResourceHelper.convertStreamToSQL(is);
		want.string(sql).contains("insert into tdd_user").notContain("--").notContain("#");
	}

	@Test
	public void testIsResourceExists_FileInSamePackage() {
		boolean isExists = ResourceHelper.isResourceExists(ResourceHelper.class, "ResourceHelper.class");
		want.bool(isExists).isEqualTo(true);

		isExists = ResourceHelper.isResourceExists(ResourceHelper.class, "executeFile.sql");
		want.bool(isExists).isEqualTo(true);
	}

	@Test
	public void testIsResourceExists_FileInSubPackage() {
		boolean isExists = ResourceHelper.isResourceExists(ResourceHelperTest.class, "sub/test.file");
		want.bool(isExists).isEqualTo(true);
	}

	@Test
	public void testIsResourceExists_FileUnexisted() {
		boolean isExists = ResourceHelper.isResourceExists(ResourceHelper.class, "unexists.file");
		want.bool(isExists).isEqualTo(false);
	}

	@Test
	public void testCopyClassPathResource() {
		String targetFile = "target/test/test.css";
		new File(targetFile).delete();
		ResourceHelper.copyClassPathResource("org/jtester/utility/text-diff.css", targetFile);
		want.file(targetFile).isExists();
	}

	@Test
	public void testGetSuffixPath() {
		String path = ResourceHelper.getSuffixPath(new File("src/java/org/jtester"), System.getProperty("user.dir")
				+ "/src/java/org/jtester/reflector/helper/ClazzHelper.java");
		want.string(path).isEqualTo("reflector/helper/ClazzHelper.java");

		path = ResourceHelper.getSuffixPath(new File("src/java/org/jtester/"), System.getProperty("user.dir")
				+ "/src/java/org/jtester/reflector/helper/ClazzHelper.java");

		want.string(path).isEqualTo("reflector/helper/ClazzHelper.java");
	}

	@Test
	public void testGetResourceUrl() {
		URL url = ResourceHelper.getResourceUrl("classpath:org/jtester/utility/log4j.xml");
		want.string(url.toString()).contains("org/jtester/utility/log4j.xml");
	}

}
