package org.test4j.module.inject;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

public class InjectModuleTest2 extends Test4J {

    OuterClaz outer = new OuterClaz();

    @Inject(targets = "outer")
    InnerClaz inner;

    @Test
    public void testTest4JInject() {
        Object o1 = outer.inner;
        want.exception(() -> o1.toString()
                , NullPointerException.class);

        this.inner = new InnerClaz();
        o1.toString();
    }

    private static class OuterClaz {
        InnerClaz inner;
    }

    private static class InnerClaz {

    }
}