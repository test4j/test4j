package org.test4j.testng.spring.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mockit.Mock;

import org.test4j.module.spring.annotations.AutoBeanInject.BeanMap;
import org.test4j.module.spring.exception.FindBeanImplClassException;
import org.test4j.module.spring.strategy.ImplementorFinder;
import org.test4j.module.spring.strategy.ImplementorFinderEx;
import org.test4j.testng.Test4J;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = "test4j")
@SuppressWarnings({ "rawtypes" })
public class ImplementorFinderTest extends Test4J {

    @Test
    public void testSplitIntfClazzByExpression() {
        String[] exps = { "org.test4j.", ".*Service" };
        String[] packs = ImplementorFinderEx.splitIntfClazzByExpression("org.test4j.service.UserService", exps);
        want.array(packs).sizeEq(3).reflectionEq(new String[] { "org.test4j.", "service", ".UserService" });
    }

    @Test(dataProvider = "testRegMatch_dataPorvider")
    public void testRegMatch(String interfaceKey, String interfaceClass, boolean match) {
        boolean ret = ImplementorFinderEx.regMatch(interfaceKey, interfaceClass);
        want.bool(ret).isEqualTo(match);
    }

    @DataProvider
    public Object[][] testRegMatch_dataPorvider() {
        return new Object[][] { { "org.test4j.**.service.*", "org.test4j.service.UserService", true }, /** <br> */
        { "org.test4j.**.*", "org.test4j.service.UserService", true }, /** <br> */
        { "org.test4j1.**.*", "org.test4j.service.UserService", false }, /** <br> */
        { "org.test4j.**.service.*", "org.test4j.my.service.UserService", true }, /** <br> */
        { "**.test4j.**.*", "org.test4j.my.service.UserService", true }, /** <br> */
        { "**.test4j1.**.*", "org.test4j.my.service.UserService", false }, /** <br> */
        { "**.test4j.**.*Service", "org.test4j.my.service.UserService", true }, /** <br> */
        { "**.test4j.**.User*", "org.test4j.my.service.UserService", true } /** <br> */
        };
    }

    @Test(dataProvider = "testReplace_dataProvider")
    public void testReplace(String intfExpress, String implExpress, String intfClazz, String implClazz) {
        String implementClass = ImplementorFinderEx.replace(intfExpress, implExpress, intfClazz);
        want.string(implementClass).isEqualTo(implClazz);
    }

    @DataProvider
    public Object[][] testReplace_dataProvider() {
        return new Object[][] {
                { "org.test4j.**.*", "org.test4j.**.*Impl", "org.test4j.service.UserService",
                        "org.test4j.service.UserServiceImpl" },
                /** <br> */
                { "org.test4j.**.*", "org.test4j.**.impl.*Impl", "org.test4j.service.UserService",
                        "org.test4j.service.impl.UserServiceImpl" },
                /** <br> */
                { "**.*", "**.impl.*Impl", "org.test4j.service.UserService", "org.test4j.service.impl.UserServiceImpl" },
                /** <br> */
                { "**.*Dao", "**.impl.*DaoImpl", "org.test4j.service.UserDao", "org.test4j.service.impl.UserDaoImpl" } };
    }

    @Test(description = "测试class名称的大小写有差异时，查找实现抛出的是错误NoClassDefFoundError，而不是异常NoClassDefFoundException的case")
    public void testGetImplClass() {
        BeanMap beanMap = ICharacterDiff.class.getAnnotation(BeanMap.class);
        try {
            reflector.invokeStatic(ImplementorFinder.class, "getImplClass", ICharacterDiff.class,
                    Arrays.asList(beanMap));
            want.fail();
        } catch (Exception e) {
            String err = e.getMessage();
            want.string(err).contains("FindBeanImplClassException")
                    .contains("ImplementorFinderTest$ICharacterDiffImpl");
        }
    }

    @BeanMap(intf = "**.*", impl = "**.*Impl")
    public static interface ICharacterDiff {

    }

    public static class ICharacterdiffImpl implements ICharacterDiff {

    }

    public static class NotPublicConstruction implements ICharacterDiff {
        private NotPublicConstruction() {

        }
    }

    public static class NoHasDefaultConstruction implements ICharacterDiff {
        public NoHasDefaultConstruction(String d) {

        }
    }

    @Test(description = "测试实现类只要有默认的构造函数，而不敢是否是private还是public的，都可以通过spring注册")
    public void testFindImplClazz_() throws FindBeanImplClassException {
        new MockUp<ImplementorFinder>() {

            @Mock
            public Class getImplClass(final Class beanClazz, final List<BeanMap> beanMapping) {
                return NotPublicConstruction.class;
            }
        };
        Class clazImpl = ImplementorFinder.findImplClazz(ImplementorFinder.class, "tet", ICharacterDiff.class,
                new ArrayList<BeanMap>());
        want.object(clazImpl).isEqualTo(NotPublicConstruction.class);
    }

    @Test(description = "测试实现类没有默认的构造函数，无法通过spring注册")
    public void testFindImplClazz_NoHasDefaultConstruction() throws FindBeanImplClassException {
        new MockUp<ImplementorFinder>() {

            @Mock
            public Class getImplClass(final Class beanClazz, final List<BeanMap> beanMapping) {
                return NoHasDefaultConstruction.class;
            }
        };
        try {
            ImplementorFinder.findImplClazz(ImplementorFinder.class, "tet", ICharacterDiff.class,
                    new ArrayList<BeanMap>());
            want.fail();
        } catch (Exception e) {
            want.object(e).clazIs(FindBeanImplClassException.class);
        }
    }

}
