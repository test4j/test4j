package org.test4j.tools.commons;

import org.junit.jupiter.api.Test;
import org.test4j.hamcrest.matcher.string.StringMode;
import org.test4j.module.ICore;

import java.io.File;
import java.net.URL;

public class ResourceHelperTest implements ICore {

    @Test
    public void getFileEncodingCharset_utf() {
        File utf8 = new File("../test4j-btest/src/main/resources/org/test4j/tools/commons/is_utf8.txt");
        String encoding = ResourceHelper.getFileEncodingCharset(utf8);
        want.string(encoding).isEqualTo("UTF-8", StringMode.IgnoreCase);
    }

    @Test
    public void getFileEncodingCharset_gbk() {
        File gbk = new File("../test4j-btest/src/main/resources/org/test4j/tools/commons/is_gbk.txt");
        String encoding = ResourceHelper.getFileEncodingCharset(gbk);
        want.string(encoding).isEqualTo("GB2312");
    }


    @Test
    public void testIsResourceExists_FileInSamePackage() {
        boolean isExists = ResourceHelper.isResourceExists(ResourceHelper.class, "ResourceHelper.class");
        want.bool(isExists).isEqualTo(true);

        isExists = ResourceHelper.isResourceExists(ResourceHelper.class, "is_utf8.txt");
        want.bool(isExists).isEqualTo(true);
    }

    @Test
    public void testIsResourceExists_FileInSubPackage() {
        boolean isExists = ResourceHelper.isResourceExists(ResourceHelperTest.class, "sub/test.file");
        want.bool(isExists).isEqualTo(true);
    }

    @Test
    public void testIsResourceExists_FileUnexisted() {
        boolean isExists = ResourceHelper.isResourceExists(ResourceHelper.class, "unexists.file");
        want.bool(isExists).isEqualTo(false);
    }

    @Test
    public void testCopyClassPathResource() {
        String targetFile = "target/test/test.css";
        new File(targetFile).delete();
        ResourceHelper.copyClassPathResource("org/test4j/utility/text-diff.css", targetFile);
        want.file(targetFile).isExists();
    }

    @Test
    public void testGetSuffixPath() {
        String path = ResourceHelper.getSuffixPath(new File("src/java/org/test4j"), System.getProperty("user.dir")
                + "/src/java/org/test4j/reflector/helper/ClazzHelper.java");
        want.string(path).isEqualTo("reflector/helper/ClazzHelper.java");

        path = ResourceHelper.getSuffixPath(new File("src/java/org/test4j/"), System.getProperty("user.dir")
                + "/src/java/org/test4j/reflector/helper/ClazzHelper.java");

        want.string(path).isEqualTo("reflector/helper/ClazzHelper.java");
    }

    @Test
    public void testGetResourceUrl() {
        URL url = ResourceHelper.getResourceUrl("classpath:org/test4j/utility/log4j.xml");
        want.string(url.toString()).contains("org/test4j/utility/log4j.xml");
    }

}
