package org.jtester.tools.commons;

import java.util.List;

import org.jtester.junit.JTester;
import org.junit.Test;

/**
 * this test will break for EclEmma Test
 * 
 * @author darui.wudr
 */
@SuppressWarnings({ "rawtypes" })
public class ClazzFinderTest implements JTester {
    @Test
    public void findClazz_1() {
        String packagename = ClazzFinder.class.getPackage().getName();
        want.string(packagename).isEqualTo("org.jtester.tools.commons");
        List<String> clazz = ClazzFinder.findClazz(packagename);
        want.collection(clazz).sizeGe(7);
    }

    @Test
    public void findClazz() {
        Class claz = ClazzFinder.class;
        List<String> clazz = ClazzFinder.findClazz(claz);
        want.collection(clazz).sizeGe(4);
        want.collection(clazz).allItemsMatchAll(the.string().regular("org\\.jtester\\.tools\\.commons\\..*"));
    }

    @Test
    public void findTestClaz() {
        Class claz = ClazzFinder.class;
        List<String> clazz = MethodFinder.findTestClaz(claz);
        int size = clazz.size();
        want.number(size).in(1, 2);
        want.collection(clazz).allItemsMatchAll(the.string().regular("org\\.jtester\\.tools\\.commons\\..*"));
    }
}
