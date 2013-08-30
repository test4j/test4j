package org.jtester.module.dbfit.utility;

import java.io.File;
import java.io.FileNotFoundException;

import org.jtester.tools.commons.ClazzHelper;
import org.jtester.tools.commons.ResourceHelper;
import org.jtester.tools.commons.StringHelper;

@SuppressWarnings("rawtypes")
public class WikiFile {
	/**
	 * fit文件的后缀
	 */
	private final static String FIT_SURFIX = ".wiki";

	private String wikiName;

	private String wikiUrl;

	private String htmlFilePath = null;

	/**
	 * 判断文件文件是否是fit文件
	 * 
	 * @param filename
	 * @return
	 */
	public static boolean isFitFile(String filename) {
		if (StringHelper.isBlankOrNull(filename)) {
			return false;
		}
		filename = filename.trim();
		if (filename.endsWith(FIT_SURFIX) == false) {
			return false;
		}
		return filename.length() > FIT_SURFIX.length();
	}

	/**
	 * 创建wiki测试结果输出路径
	 * 
	 * @param dbfitDir
	 * @return
	 */
	public File mkTestedDir(String dbfitDir) {
		int last = wikiUrl.lastIndexOf(FIT_SURFIX);
		String base = System.getProperty("user.dir");
		this.htmlFilePath = String.format("%s/%s/%s.html", base, dbfitDir, wikiUrl.subSequence(0, last));

		File html = new File(this.htmlFilePath);
		ResourceHelper.mkFileParentDir(html);
		return html;
	}

	public String readWikiContent() throws FileNotFoundException {
		String content = ResourceHelper.readFromFile(this.wikiUrl);
		return content;
	}

	/**
	 * 查找wiki文件
	 * 
	 * @param claz
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static WikiFile findWikiFile(final Class claz, final String url) throws Exception {
		WikiFile wikiFile = new WikiFile();

		wikiFile.wikiName = url.replaceAll(".*[\\/\\\\]", "").replaceFirst(".wiki", "");

		if (claz != null && ResourceHelper.isResourceExists(claz, url)) {
			wikiFile.wikiUrl = ClazzHelper.getPathFromPath(claz) + "/" + url;
		} else if (ResourceHelper.isResourceExists(null, url)) {
			wikiFile.wikiUrl = url;
		} else {
			if (claz == null) {
				throw new RuntimeException(String.format("can't find wiki in classpath:%s", url));
			} else {
				throw new RuntimeException(String.format("can't find wiki in classpaths:%s and %s",
						ClazzHelper.getPathFromPath(claz) + "/" + url, url));
			}
		}

		return wikiFile;
	}

	public String getHtmlFilePath() {
		return htmlFilePath;
	}

	/**
	 * 返回wiki文件的名称
	 * 
	 * @return
	 */
	public String wikiName() {
		return wikiName;
	}

	public String getWikiUrl() {
		return wikiUrl;
	}
}
