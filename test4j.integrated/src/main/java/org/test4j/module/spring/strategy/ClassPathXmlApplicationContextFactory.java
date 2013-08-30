package org.test4j.module.spring.strategy;

import java.util.List;

public class ClassPathXmlApplicationContextFactory implements ApplicationContextFactory {

    /**
     * Create an <code>ClassPathXmlApplicationContext</code> for the given
     * locations on which refresh has not yet been called
     * 
     * @param locations The configuration file locations, not null
     * @return A context, on which the <code>refresh()</code> method hasn't been
     *         called yet
     */
    public Test4JSpringContext createApplicationContext(List<String> locations, boolean refresh, boolean allowLazy) {
        if (allowLazy) {
            Test4JSpringContext c = new LazySpringContext(locations.toArray(new String[0]), refresh);
            return c;
        } else {
            Test4JSpringContext c = new Test4JSpringContext(locations.toArray(new String[0]), refresh);
            return c;
        }
    }
}
