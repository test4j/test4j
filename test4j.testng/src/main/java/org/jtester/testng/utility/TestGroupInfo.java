package org.jtester.testng.utility;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jtester.tools.commons.ResourceHelper;
import org.jtester.tools.commons.StringHelper;
import org.testng.ITestNGMethod;

/**
 * 测试类的分组信息统计
 * 
 * @author darui.wudr
 * 
 */
public class TestGroupInfo {
	final static String UNGROUP_TEST = "ungroup";
	private Map<String, TestClazzInfoByGroup> userMethods;// <GroupName,ClazzMethodInfo>

	TestGroupInfo() {
		this.userMethods = new HashMap<String, TestClazzInfoByGroup>();
	}

	private Set<TestMethodInfo> getTestMethodInfoByGroupAndClazz(String groupName, String clazzName) {
		TestClazzInfoByGroup clazzInfo = this.userMethods.get(groupName);
		if (clazzInfo == null) {
			clazzInfo = new TestClazzInfoByGroup(groupName);
			this.userMethods.put(groupName, clazzInfo);
		}
		Set<TestMethodInfo> testMethodInfo = clazzInfo.getTestMethodInfosByClazzName(clazzName);

		return testMethodInfo;
	}

	public void addTestMethods(Set<String> groupNames, ITestNGMethod testMethod, String status) {
		if (testMethod.isTest() == false) {
			return;
		}
		String clazzName = testMethod.getRealClass().getName();
		TestMethodInfo methodInfo = TestMethodInfo.instance(testMethod, groupNames, status);
		if (groupNames == null || groupNames.size() == 0) {
			Set<TestMethodInfo> methodInfos = getTestMethodInfoByGroupAndClazz(UNGROUP_TEST, clazzName);
			methodInfos.add(methodInfo);
		}
		for (String groupName : groupNames) {
			Set<TestMethodInfo> methodInfos = getTestMethodInfoByGroupAndClazz(groupName, clazzName);
			methodInfos.add(methodInfo);
		}
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (Map.Entry<String, TestClazzInfoByGroup> entry : userMethods.entrySet()) {
			buffer.append("group name = " + entry.getKey() + "\n");
			buffer.append(entry.getValue().toString() + "\n");
		}
		return buffer.toString();
	}

	/**
	 * html报表形式
	 * 
	 * @return
	 */
	public String htmlReport() {
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("<html><head>");
			buffer.append(String.format("<META HTTP-EQUIV='Content-Type' CONTENT='text/html; charset=%s'>",
					ResourceHelper.defaultFileEncoding()));
			buffer.append("<style>");
			InputStream is = ResourceHelper.getResourceAsStream("org/jtester/testng/UserTestReporter.css");
			if (is != null) {
				String style = ResourceHelper.readFromStream(is);
				buffer.append(style);
			}
			buffer.append("</style></head>");
			buffer.append("<table>");
			for (Map.Entry<String, TestClazzInfoByGroup> userMethod : this.userMethods.entrySet()) {
				buffer.append(userMethod.getValue().htmlReport());
			}
			buffer.append("</table>");
			buffer.append("</html>");
			return buffer.toString();
		} catch (Throwable e) {
			return StringHelper.exceptionTrace(e);
		}
	}
}