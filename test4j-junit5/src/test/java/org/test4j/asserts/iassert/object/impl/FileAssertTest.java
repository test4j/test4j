package org.test4j.asserts.iassert.object.impl;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

import java.io.File;
import java.util.UUID;

public class FileAssertTest extends Test4J {
    @Test
    public void isExists_AssertionError() {
        String tmp = System.getProperty("java.io.tmpdir");
        String file = tmp + File.separatorChar + UUID.randomUUID().toString() + ".txt";
        want.exception(() ->
                want.file(new File(file)).isExists(), AssertionError.class);
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
