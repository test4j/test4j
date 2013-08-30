package org.jtester.junit.extend.demo;

public class SampleLoggingInterceptor implements Interceptor {
	Timer timer = new Timer();

	public void interceptBefore() {
		System.out.println("Interceptor started.");
		timer.start();
	}

	public void interceptAfter() {
		timer.stop();
		System.out.println("Interceptor ended. The test executed for " + timer.time());
	}

	class Timer {
		private long nanoStart = 1;
		private long nanoEnd = 0;

		void start() {
			nanoStart = System.nanoTime();
		}

		void stop() {
			nanoEnd = System.nanoTime();
		}

		long time() {
			return nanoEnd - nanoStart;
		}
	}
}
