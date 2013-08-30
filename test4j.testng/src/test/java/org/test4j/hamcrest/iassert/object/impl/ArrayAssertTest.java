package org.test4j.hamcrest.iassert.object.impl;

import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = { "test4j", "assertion" })
public class ArrayAssertTest extends Test4J {
    public void hasItems() {
        want.array(new String[] { "first item", "second item", "third item" }).hasAllItems("first item", "second item");
    }
}
