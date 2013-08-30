package org.test4j.module.core;

import org.test4j.module.inject.annotations.Inject;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

public class InjectModuleTest extends Test4J {

    OuterClaz outer = new OuterClaz();

    @Inject(targets = "outer")
    InnerClaz inner;

    @Test
    public void testTest4JInject() {
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
