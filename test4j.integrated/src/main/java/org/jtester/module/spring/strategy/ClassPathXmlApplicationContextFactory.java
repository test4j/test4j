package org.jtester.module.spring.strategy;

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
    public JTesterSpringContext createApplicationContext(List<String> locations, boolean refresh, boolean allowLazy) {
        if (allowLazy) {
            JTesterSpringContext c = new LazySpringContext(locations.toArray(new String[0]), refresh);
            return c;
        } else {
            JTesterSpringContext c = new JTesterSpringContext(locations.toArray(new String[0]), refresh);
            return c;
        }
    }
}
