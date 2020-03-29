package org.test4j.tools.commons;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

public class ClazzHelperTest_NewInstance extends Test4J {
    @Test
    public void testNewInstance_Abstract() {
        try {
            ClazzHelper.newInstance(AbstractClazz.class);
            want.fail();
        } catch (Exception e) {
            String message = e.getMessage();
            want.string(message).contains("unsupport");
        }
    }

    @Test
    public void testNewInstance_DefautClazz() {
        DefautClazz o = ClazzHelper.newInstance(DefautClazz.class);
        want.string(o.name).isEqualTo("default");
    }

    @Test
    public void testNewInstance_ParamClazz() {
        ParamClazz o = ClazzHelper.newInstance(ParamClazz.class);
        want.string(o.name).isNull();
    }

    @Test
    public void testNewInstance_PrivateClazz() {
        PrivateClazz o = ClazzHelper.newInstance(PrivateClazz.class);
        want.string(o.name).isEqualTo("private");
    }

    @Test
    public void testNewInstance_SubPrivateClazz() {
        SubPrivateClazz o = ClazzHelper.newInstance(SubPrivateClazz.class);
        want.string(o.name).isEqualTo("private");
    }
}

abstract class AbstractClazz {

}

interface IClazz {

}

class DefautClazz {
    String name = null;

    public DefautClazz() {
        this.name = "default";
    }
}

class ParamClazz {
    String name = null;

    public ParamClazz(String name) {
        this.name = name;
    }
}

class PrivateClazz {
    String name = null;

    protected PrivateClazz() {
        this.name = "private";
    }
}

class SubPrivateClazz extends PrivateClazz {

}
