package org.jtester.testng.utility;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.testng.ITestNGMethod;

/**
 * 测试方法的clazz信息统计
 * 
 * @author darui.wudr
 * 
 */
public class TestClazzInfoByGroup {
	private String groupName;
	private Map<String, Set<TestMethodInfo>> methodInfos;// <ClazzName,TestMethods>

	public TestClazzInfoByGroup(String groupName) {
		this.groupName = groupName;
		this.methodInfos = new HashMap<String, Set<TestMethodInfo>>();
	}

	public Set<TestMethodInfo> getTestMethodInfosByClazzName(String clazzName) {
		Set<TestMethodInfo> testMethodInfo = this.methodInfos.get(clazzName);
		if (testMethodInfo == null) {
			testMethodInfo = new HashSet<TestMethodInfo>();
			this.methodInfos.put(clazzName, testMethodInfo);
		}
		return testMethodInfo;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (Map.Entry<String, Set<TestMethodInfo>> entry : methodInfos.entrySet()) {
			buffer.append("clazz " + entry.getKey() + ",Test Method Infos:\n");
			for (TestMethodInfo info : entry.getValue()) {
				buffer.append("\t" + info.toString() + "\n");
			}
		}
		return buffer.toString();
	}

	/**
	 * 返回分组中测试方法的总个数
	 * 
	 * @return
	 */
	public int countMethods() {
		int count = 0;
		for (Map.Entry<String, Set<TestMethodInfo>> entry : this.methodInfos.entrySet()) {
			count += entry.getValue().size();
		}
		return count;
	}

	public String getGroupName() {
		return groupName;
	}

	// private static String TEST_REPORT_DIR = "../test.report";

	public String htmlReport() {
		StringBuffer buffer = new StringBuffer();
		boolean isFirstClazz = true;
		int total = countMethods();
		buffer.append(String.format("<tr><td rowspan=%d>%s count:%d</td>", total, this.groupName, total));
		for (Map.Entry<String, Set<TestMethodInfo>> clzzInfo : this.methodInfos.entrySet()) {
			if (isFirstClazz) {
				isFirstClazz = false;
			} else {
				buffer.append("<tr>");
			}
			Set<TestMethodInfo> methodInfos = clzzInfo.getValue();
			String clazzName = clzzInfo.getKey();
			// String javaFile = String.format("%s/%s.html", TEST_REPORT_DIR,
			// clazzName);
			buffer.append(String.format("<td rowspan=%d><a title='%s'>%s</a></td>", methodInfos.size(), clazzName,
					clazzName.replaceAll("[^\\.]+\\.", "")));
			boolean isFirstClazzMethod = true;
			for (TestMethodInfo method : methodInfos) {
				if (isFirstClazzMethod) {
					isFirstClazzMethod = false;
				} else {
					buffer.append("<tr>");
				}
				buffer.append(String.format("<td class='%s'>%s</td>", method.getStatus(), method.getMethodName()));
				buffer.append(String.format("<td>%s<td>", method.getGroups().toString()));
				buffer.append("</tr>\n");
			}
		}
		return buffer.toString();
	}
}

class TestMethodInfo {
	private String status;
	private String clazzName;

	private String methodName;

	private Set<String> groups = new HashSet<String>();

	private String javaDoc;

	public String getClazzName() {
		return clazzName;
	}

	public String getMethodName() {
		return methodName;
	}

	public Set<String> getGroups() {
		return groups;
	}

	public String getJavaDoc() {
		return javaDoc;
	}

	public String getStatus() {
		return status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestMethodInfo other = (TestMethodInfo) obj;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		return true;
	}

	public static TestMethodInfo instance(ITestNGMethod testMethod, Set<String> groups, String status) {
		TestMethodInfo methodInfo = new TestMethodInfo();
		methodInfo.clazzName = testMethod.getRealClass().getName();
		methodInfo.methodName = testMethod.getMethodName();
		methodInfo.groups = groups;
		methodInfo.javaDoc = "todo";// TODO
		methodInfo.status = status;

		return methodInfo;
	}

	@Override
	public String toString() {
		return "MethodInfo [clazzName=" + clazzName + ", groups=" + groups + ", javaDoc=" + javaDoc + ", methodName="
				+ methodName + "]";
	}
}
