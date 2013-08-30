package org.jtester.testng.report;

import org.jtester.testng.report.UserTestReporter.Status;
import org.jtester.tools.commons.StringHelper;

public class MethodTestDto {
	public final static String UN_GROUP_NAME = "ungroup";
	private String methodName;

	private String clazzName;

	private Status status;

	private long duration;

	private String groups;

	private long threadId;

	public MethodTestDto(String methodName, String clazzName, Status status, long duration, String[] groups) {
		this.methodName = methodName;
		this.clazzName = clazzName;
		this.status = status;
		this.duration = duration;
		if (groups == null || groups.length == 0) {
			this.groups = UN_GROUP_NAME;
		} else {
			this.groups = StringHelper.join(",", groups);
		}
		this.threadId = Thread.currentThread().getId();
	}

	public String getMethodName() {
		return methodName;
	}

	public Status getStatus() {
		return status;
	}

	public long getDuration() {
		return duration;
	}

	public String getGroups() {
		return groups;
	}

	public long getThreadId() {
		return threadId;
	}

	public String getClazzName() {
		return clazzName;
	}

	public String getSpeedCss() {
		if (duration < 10 * 1000L) {
			return "green";// green style
		} else if (10 * 1000L <= duration && duration <= 20 * 1000L) {
			return "yellow";// yellow style
		} else {
			return "red";// red style
		}
	}
}
