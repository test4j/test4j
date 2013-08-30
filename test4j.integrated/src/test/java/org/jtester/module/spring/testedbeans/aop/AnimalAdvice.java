package org.jtester.module.spring.testedbeans.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jtester.module.core.utility.MessageHelper;

public class AnimalAdvice implements MethodInterceptor {

	public Object invoke(MethodInvocation invocation) throws Throwable {
		StringBuilder sb = new StringBuilder();
		sb.append("Target Class:").append(invocation.getThis()).append("\n").append(invocation.getMethod())
				.append("\n");
		Object retVal = invocation.proceed();
		sb.append(" return value:").append(retVal).append("\n");
		MessageHelper.info(sb.toString());
		return retVal;
	}
}
