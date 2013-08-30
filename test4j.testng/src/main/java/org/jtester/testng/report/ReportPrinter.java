package org.jtester.testng.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.jtester.module.core.utility.MessageHelper;
import org.jtester.testng.report.UserTestReporter.Status;
import org.jtester.tools.commons.ResourceHelper;

/**
 * ReportPrinter
 * 
 * @author zili.dengzl
 * @author darui.wudr
 * 
 * @2010.12.28
 */
public class ReportPrinter {

	public static void printHtmlReport(final Map<String, UserGroupDto> userGroups, final List<MethodTestDto> methods) {
		try {
			File htmlFile = getHtmlReportFile();
			FileWriter writer = new FileWriter(htmlFile);
			writer.write(genHead());
			writer.write("\n");
			writer.write("<body>\n");
			writer.write(genFirstPart(userGroups));
			writer.write("\n");
			writer.write("\n");
			writer.write(genSecondPart(methods));
			writer.write("</body>\n");
			writer.write("</html>");
			writer.close();
		} catch (Throwable e) {
			MessageHelper.warn("print user group report error:" + e.getMessage());
		}
	}

	private static File getHtmlReportFile() {
		File htmlFile = new File(System.getProperty("user.dir") + "/target/UserTestMethods.html");
		ResourceHelper.mkFileParentDir(htmlFile);
		return htmlFile;
	}

	/**
	 * 生成测试报表的第一部分<br>
	 * 格式<br>
	 * |user name|success count|failure count|skipped count| <br>
	 * 
	 * @param userGroups
	 * @return
	 */
	private static String genFirstPart(final Map<String, UserGroupDto> userGroups) {
		StringBuffer buff = new StringBuffer();

		buff.append("<table>\n<tr><td colspan='5'>user test case statistics</td></tr>\n");
		buff.append("<tr><td>group name</td><td>success count</td><td>failure count</td><td>skipped count</td><td>total</td></tr>\n");

		for (UserGroupDto dto : userGroups.values()) {
			buff.append("<tr><td style='text-align:left;'>").append(dto.getGroupName()).append("</td>");
			buff.append("<td class='green'>").append(dto.getSuccess()).append("</td>");
			buff.append("<td class='red'>").append(dto.getFailure()).append("</td>");
			buff.append("<td class='gray'>").append(dto.getSkipped()).append("</td>");
			buff.append("<td>").append(dto.getTotal()).append("</td>");

			buff.append("</tr>\n");
		}

		buff.append("</table>\n");
		return buff.toString();
	}

	/**
	 * @param sb
	 * @return
	 */
	private static String genSecondPart(final List<MethodTestDto> methods) {
		StringBuffer buff = new StringBuffer();

		buff.append("<table><tr><td colspan=4>test case running sequence</td></tr>\n");
		buff.append("<tr><td>method name</td><td>test status</td><td>time cost</td><td>groups</td></tr>\n");

		for (MethodTestDto method : methods) {
			buff.append("<tr>");
			buff.append("<td style='text-align:left;'>");
			buff.append(method.getMethodName());
			buff.append("<br/>");
			buff.append(method.getClazzName());
			buff.append("</td>");
			Status status = method.getStatus();
			buff.append(String.format("<td class='%s'>%s</td>", status.css(), status.name()));
			buff.append(String.format("<td class='%s'>%d ms</td>", method.getSpeedCss(), method.getDuration()));
			buff.append("<td style='text-align:left;'>").append(method.getGroups()).append("</td>");
			buff.append("</tr>\n");
		}

		buff.append("</table>\n");

		return buff.toString();
	}

	private static String genHead() throws FileNotFoundException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<html>\n\t<head>\n");
		buffer.append(String.format("<META HTTP-EQUIV='Content-Type' CONTENT='text/html; charset=%s'>\n",
				ResourceHelper.defaultFileEncoding()));
		buffer.append("<style>\n");
		InputStream is = ResourceHelper.getResourceAsStream("org/jtester/testng/test-report.css");
		if (is != null) {
			String style = ResourceHelper.readFromStream(is);
			buffer.append(style);
		}
		buffer.append("</style>\n</head>\n");
		return buffer.toString();
	}
}
