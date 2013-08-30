package org.jtester.testng.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * 生成测试用例报表<br>
 * 格式: <br>
 * |user name|success count|failure count|skipped count| <br>
 * <br>
 * <br>
 * |method name|status|duration time|thread id|<br>
 * 
 * @author zili.dengzl
 * @author darui.wudr
 * 
 */
public class UserTestReporter extends TestListenerAdapter {
	private final static List<MethodTestDto> methods = new ArrayList<MethodTestDto>();

	private final static Map<String, UserGroupDto> userGroups = new HashMap<String, UserGroupDto>();

	@Override
	public void onTestSuccess(ITestResult result) {
		addResult(result, Status.success);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		addResult(result, Status.failure);
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		addResult(result, Status.skipped);
	}

	/**
	 * 生成报表
	 */
	@Override
	public void onFinish(ITestContext testContext) {
		ReportPrinter.printHtmlReport(userGroups, methods);
	}

	private static void addResult(ITestResult result, Status status) {
		ITestNGMethod method = result.getMethod();
		if (method.isTest() == false) {
			return;
		}
		long duration = result.getEndMillis() - result.getStartMillis();
		String[] groups = method.getGroups();
		String methodName = method.getMethodName();
		String clazzName = method.getRealClass().getName();
		methods.add(new MethodTestDto(methodName, clazzName, status, duration, groups));

		if (groups == null) {
			UserGroupDto userDto = getUserGroupDto(MethodTestDto.UN_GROUP_NAME);
			userDto.addResult(status);
			return;
		}
		for (String group : groups) {
			UserGroupDto userDto = getUserGroupDto(group);
			userDto.addResult(status);
		}
	}

	private static UserGroupDto getUserGroupDto(String group) {
		UserGroupDto userDto = userGroups.get(group);
		if (userDto == null) {
			userDto = new UserGroupDto(group);
			userGroups.put(group, userDto);
		}
		return userDto;
	}

	public static enum Status {
		success {
			@Override
			public String css() {
				return "green";
			}
		},
		failure {
			@Override
			public String css() {
				return "red";
			}
		},
		skipped {
			@Override
			public String css() {
				return "gray";
			}
		};

		public abstract String css();
	}
}
