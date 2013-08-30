package org.jtester.hamcrest.iassert.object.impl;

import org.jtester.junit.JTester;
import org.junit.Test;

public class ArrayAssertTest implements JTester {
    @Test
    public void hasItems() {
        want.array(new String[] { "first item", "second item", "third item" }).hasAllItems("first item", "second item");
    }
}
