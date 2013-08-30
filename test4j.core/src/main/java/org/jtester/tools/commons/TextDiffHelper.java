package org.jtester.tools.commons;

import java.io.FileNotFoundException;
import java.util.LinkedList;

import ext.jtester.apache.commons.lang.StringEscapeUtils;

import org.jtester.tools.commons.TextDiffMatchPatch.Diff;
import org.jtester.tools.commons.TextDiffMatchPatch.Operation;

public class TextDiffHelper {
	private static String style = "";
	static {
		StringBuffer buff = new StringBuffer();
		buff.append("<style type=\"text/css\" media=\"all\">\n");
		try {
			String css = ResourceHelper.readFromFile("org/jtester/utility/text-diff.css");
			buff.append(css);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		buff.append("</style>\n");
		style = buff.toString();
	}
	static TextDiffMatchPatch textDiff = new TextDiffMatchPatch();

	/**
	 * 比较左边文件内容和右边文件内容的区别，并且返回比较差异内容的html
	 * 
	 * @param lText
	 * @param rText
	 * @return
	 */
	public static String getDiffHtml(String lText, String rText) {
		LinkedList<Diff> diffs = textDiff.diff_main(lText, rText);
		String compare = textDiff.diff_prettyHtml(diffs);

		StringBuilder buff = new StringBuilder();
		buff.append("<html>\n");
		buff.append("<head>\n");
		buff.append(style);
		buff.append("</head>\n");
		buff.append("<body>\n");

		buff.append("<div id=\"topPanel\">\n");
		String diffSize = getDiffSize(diffs);
		buff.append(diffSize);
		printDiffText(buff, compare);
		buff.append("</div>\n");
		buff.append("<div id=\"vSplitter\"></div>\n");
		buff.append("<div>\n");
		buff.append("<div id=\"leftPanel\" class=\"buttom buttomPanel\">\n");
		printHtmlText(buff, lText);
		buff.append("</div>\n");
		buff.append("<div id=\"hSplitter\" class=\"buttom\"></div>\n");
		buff.append("<div id=\"rightPanel\" class=\"buttom buttomPanel\">\n");
		printHtmlText(buff, rText);
		buff.append("</div>\n");
		buff.append("</div>\n");
		buff.append("</body>\n");
		buff.append("</html>");
		return buff.toString();
	}

	/**
	 * 比较左边和右边的文件，返回比较差异
	 * 
	 * @param lText
	 * @param rText
	 * @return
	 */
	public static LinkedList<Diff> getDiff(String lText, String rText) {
		LinkedList<Diff> diffs = textDiff.diff_main(lText, rText);
		return diffs;
	}

	static String getDiffSize(LinkedList<Diff> diffs) {
		int count = 0;
		for (Diff diff : diffs) {
			if (diff.operation == Operation.EQUAL) {
				continue;
			}
			count++;
		}
		if (count == 0) {
			return "<p>No Difference.</p>";
		} else if (count == 1) {
			return "<p>" + diffs.size() + " difference.</p>";
		} else {
			return "<p>" + diffs.size() + " differences.</p>";
		}
	}

	static void printDiffText(StringBuilder buff, String text) {
		text = text.replace("&para;", "").replace("    ", "&nbsp;&nbsp;&nbsp;&nbsp;");
		;
		buff.append(text);
	}

	static void printHtmlText(StringBuilder buff, String text) {
		text = text.replace("\n", "!@#$BR$#@!");
		text = text.replace(" ", "!@#$SP$#@!");
		text = StringEscapeUtils.escapeXml(text);
		text = text.replace("!@#$SP$#@!", "&nbsp;");
		text = text.replace("!@#$BR$#@!", "<BR/>");
		buff.append(text);
	}
}
