package org.test4j.tools.reflector;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ReflectorTest_NewInstance extends Test4J {

    @Test
    public void testNewInstance() {
        Object instance = reflector.newInstance(NoDefaultConstructor.class);
        want.object(instance).notNull();
        String result = ((ISayHello) instance).getName();
        want.string(result).isNull();
    }


    @Test
    public void testPrivateConstruction() {
        Object instance = reflector.newInstance(PrivateConstructor.class);
        want.object(instance).notNull();
        String result = ((ISayHello) instance).getName();
        want.string(result).isEqualTo("construction");
    }

    @Test
    public void testNewInstance_AbstractClazz() {
        try {
            reflector.newInstance(AbstractClazz.class);
            want.fail();
        } catch (Exception e) {
            String message = e.getMessage();
            want.string(message).contains("unsupport").contains("abstract class");
        }
    }
}

class NoDefaultConstructor implements ISayHello {
    private String name = "defualt";

    public NoDefaultConstructor(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}

class PrivateConstructor implements ISayHello {
    private String name = "defualt";

    private PrivateConstructor() {
        this.name = "construction";
    }

    @Override
    public String getName() {
        return name;
    }
}

abstract class AbstractClazz implements ISayHello {
    private String name = "defualt";

    private AbstractClazz(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}

interface ISayHello {
    public String getName();
}
