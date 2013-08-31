package org.test4j.json.decoder.single;

import java.io.File;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.junit.Test4J;

public class FileDecoderTest extends Test4J {

    @Test
    public void testDecodeSimpleValue() {
        String json = "['d:/1.txt','d:/2.txt']";
        File[] files = JSON.toObject(json, File[].class);
        want.array(files).sizeEq(2);
    }

    @Test
    public void testDecodeSimpleValue_FileArray2D() {
        String json = "[['d:/1.txt'],['d:/2.txt']]";
        File[][] files = JSON.toObject(json, File[][].class);
        want.array(files).sizeEq(2);
    }
}
