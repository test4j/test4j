package org.jtester.junit.extend.demo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class InterceptorRunner extends BlockJUnit4ClassRunner {
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface InterceptorClasses {
		public Class<?>[] value();
	}

	public InterceptorRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
	}

	@Override
	public Statement methodInvoker(FrameworkMethod method, Object test) {
		InterceptorStatement statement = new InterceptorStatement(super.methodInvoker(method, test));
		InterceptorClasses annotation = test.getClass().getAnnotation(InterceptorClasses.class);
		Class<?>[] klasez = annotation.value();
		try {
			for (Class<?> klaz : klasez) {
				statement.addInterceptor((Interceptor) klaz.newInstance());
			}
		} catch (IllegalAccessException ilex) {
			ilex.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return statement;
	}
}
