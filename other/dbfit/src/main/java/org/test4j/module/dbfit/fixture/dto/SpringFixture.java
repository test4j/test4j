package org.test4j.module.dbfit.fixture.dto;

import org.test4j.module.ICore;
import org.test4j.module.dbfit.DbFitTestedContext;
import org.test4j.module.dbfit.DbFitTestedContext.RunIn;
import org.test4j.module.dbfit.fixture.Test4JFixture;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.remote.RemoteInvokerRegister;
import org.test4j.tools.commons.AnnotationHelper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringFixture extends Test4JFixture implements ICore {
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
