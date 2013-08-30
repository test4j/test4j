package org.jtester.module.dbfit.fixture.dto;

import org.jtester.module.ICore;
import org.jtester.module.dbfit.DbFitTestedContext;
import org.jtester.module.dbfit.DbFitTestedContext.RunIn;
import org.jtester.module.dbfit.fixture.JTesterFixture;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.remote.RemoteInvokerRegister;
import org.jtester.tools.commons.AnnotationHelper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringFixture extends JTesterFixture implements ICore {
	private ClassPathXmlApplicationContext ctx;

	/**
	 * 注入spring bean
	 */
	public SpringFixture() {
		DbFitTestedContext.setRunIn(RunIn.FitNesse);

		SpringContext anotations = AnnotationHelper.getClassLevelAnnotation(SpringContext.class, this.getClass());
		if (anotations == null) {
			return;
		}
		try {
			String[] locations = anotations.value();
			// boolean shared = anotations.share();
			ctx = new FixtureSpringContext(locations, this.getClass(), false);
			FixtureBeanInjector.injectBeans(ctx, this);
			RemoteInvokerRegister.injectSpringBeanRemote(ctx, this);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException("spring inject error", e);
		}
	}
}
