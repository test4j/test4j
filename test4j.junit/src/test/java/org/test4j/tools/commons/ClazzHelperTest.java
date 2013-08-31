package org.test4j.tools.commons;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.Test;
import org.test4j.json.encoder.beans.test.TestedClazz;
import org.test4j.json.encoder.beans.test.TestedIntf;
import org.test4j.junit.Test4J;
import org.test4j.junit.annotations.DataFrom;
import org.test4j.tools.datagen.ConstructorArgsGenerator;

@SuppressWarnings("rawtypes")
public class ClazzHelperTest extends Test4J {

    @Test
    @DataFrom("provideClazzName")
    public void getPackFromClassName(String clazz, String pack) {
        want.string(ClazzHelper.getPackFromClassName(clazz)).isEqualTo(pack);
    }

    public static Object[][] provideClazzName() {
        return new String[][] { { "", "" }, { "EefErr", "" },
                { "org.test4j.utility.ClazzUtilTest", "org.test4j.utility" } };
    }

    @Test
    @DataFrom("dataProvider_testGetPathFromPath")
    public void testGetPathFromPath(String clazName, String path) {
        String _path = ClazzHelper.getPathFromPath(clazName);
        want.string(_path).isEqualTo(path);
    }

    public static Object[][] dataProvider_testGetPathFromPath() {
        return new Object[][] { { "a.b.c.ImplClazz", "a/b/c" }, // <br>
                { "ImplClazz", "" }, /** <br> **/
                { ".ImplClazz", "" }
        /** <br> **/
        };
    }

    @Test
    public void testGetBytes() {
        byte[] bytes = ClazzHelper.getBytes(ClazzHelper.class);
        want.array(bytes).notNull().sizeGt(1);
    }

    @Test
    @DataFrom("proxy_types")
    public void testGetUnProxyType(Class type, Class expected) {
        Class actual = ClazzHelper.getUnProxyType(type);
        want.object(actual).isEqualTo(expected);
    }

    public static Object[][] proxy_types() {
        Object proxy = Proxy.newProxyInstance(ClazzHelperTest.class.getClassLoader(), new Class[] { TestedIntf.class },
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return null;
                    }
                });
        return new Object[][] { { TestedClazz.class, TestedClazz.class },// <br>
                { new TestedClazz() {
                    {
                    }
                }.getClass(), TestedClazz.class }, // <br>
                { new TestedIntf() {
                    {

                    }
                }.getClass(), Object.class },// <br>
                { proxy.getClass(), Object.class } // <br>
        };
    }

    @SuppressWarnings("serial")
    @Test
    public void testNewInstance() throws Exception {
        PrivateParaConstructor result = reflector.newInstance(PrivateParaConstructor.class,
                new ConstructorArgsGenerator() {
                    @Override
                    public Object[] generate(Constructor constructor) {
                        return new Object[] { 1 };
                    }
                });
        want.object(result).reflectionEqMap(new DataMap() {
            {
                this.put("i", 1);
                this.put("str", "xxx");
            }
        });
    }

    @SuppressWarnings("unused")
    private static class PrivateParaConstructor {
        private final int    i;
        private final String str;

        private PrivateParaConstructor(int i) {
            this.i = i;
            this.str = "xxx";
        }
    }
}
