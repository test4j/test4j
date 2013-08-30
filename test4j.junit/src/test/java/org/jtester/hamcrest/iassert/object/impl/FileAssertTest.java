package org.jtester.hamcrest.iassert.object.impl;

import java.io.File;
import java.util.UUID;

import org.jtester.junit.JTester;
import org.junit.Test;

public class FileAssertTest implements JTester {
    @Test(expected = AssertionError.class)
    public void isExists_AssertionError() {
        String tmp = System.getProperty("java.io.tmpdir");
        String file = tmp + File.separatorChar + UUID.randomUUID().toString() + ".txt";
        want.file(new File(file)).isExists();
    }

    @Test
    public void unExists() {
        String tmp = System.getProperty("java.io.tmpdir");
        String file = tmp + File.separatorChar + UUID.randomUUID().toString() + ".txt";
        want.file(new File(file)).unExists();
    }

    @Test
    public void testNameContain() {
        File file = new File(System.getProperty("user.dir") + "/src/test/java");
        want.file(file).nameContain("java");
    }

    @Test
    public void testNameEq() {
        File file = new File(System.getProperty("user.dir") + "/src/test/java");
        want.file(file).nameContain("java");
    }
}
