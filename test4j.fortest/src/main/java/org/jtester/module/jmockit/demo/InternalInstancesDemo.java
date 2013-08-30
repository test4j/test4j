package org.jtester.module.jmockit.demo;

import java.util.Observable;
import java.util.concurrent.Callable;

import org.jtester.module.core.utility.MessageHelper;
import org.jtester.module.jmockit.demo.Service.ServiceImpl;

@SuppressWarnings({ "rawtypes" })
public class InternalInstancesDemo {

	private final Service service1 = new ServiceImpl();
	private final Service service2 = new Service() {
		public int doSomething() {
			return 2;
		}
	};

	Observable observable;

	public int businessOperation(final boolean b) {
		Object o = new Callable() {
			public Object call() {
				throw new IllegalStateException();
			}
		}.call();
		MessageHelper.info("callalbe call result:" + o);

		observable = new Observable() {
			{
				if (b) {
					throw new IllegalArgumentException();
				}
			}
		};

		return service1.doSomething() + service2.doSomething();
	}
}
