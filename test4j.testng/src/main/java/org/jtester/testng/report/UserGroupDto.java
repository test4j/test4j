package org.jtester.testng.report;

import org.jtester.testng.report.UserTestReporter.Status;

public class UserGroupDto {
	private String groupName;

	private int success;

	private int failure;

	private int skipped;

	public UserGroupDto(String groupName) {
		this.groupName = groupName;
		this.success = 0;
		this.failure = 0;
		this.skipped = 0;
	}

	public void addResult(Status status) {
		switch (status) {
		case success:
			success++;
			break;
		case failure:
			failure++;
			break;
		case skipped:
			skipped++;
			break;
		default:
		}
	}

	public String getGroupName() {
		return groupName;
	}

	public int getSuccess() {
		return success;
	}

	public int getFailure() {
		return failure;
	}

	public int getSkipped() {
		return skipped;
	}

	public int getTotal() {
		return this.success + this.skipped + this.failure;
	}
}
