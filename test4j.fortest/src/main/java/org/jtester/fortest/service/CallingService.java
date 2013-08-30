package org.jtester.fortest.service;

public class CallingService {
	private CalledService calledService;

	public void call(String message) {
		this.calledService.called(message);
	}

	public void expectedBoolean(boolean bl) {
		this.calledService.expectedBoolean(bl);
	}

	public CalledService getCalledService() {
		return calledService;
	}

	public void setCalledService(CalledService calledService) {
		this.calledService = calledService;
	}
}
