package org.test4j.module.core;

import org.junit.Test;
import org.test4j.junit.JTester;
import org.test4j.module.inject.annotations.Inject;

public class InjectModuleTest implements JTester {

    OuterClaz outer = new OuterClaz();

    @Inject(targets = "outer")
    InnerClaz inner;

    @Test
    public void testJtesterInject() {
        Object o1 = outer.inner;
        try {
            o1.toString();
        } catch (NullPointerException e) {
            want.object(e).clazIs(NullPointerException.class);
        } catch (Throwable e) {
            want.fail();
        }

        this.inner = new InnerClaz();
        try {
            o1.toString();
        } catch (Throwable e) {
            want.fail();
        }
    }

    private static class OuterClaz {
        InnerClaz inner;
    }

    private static class InnerClaz {

    }
}
