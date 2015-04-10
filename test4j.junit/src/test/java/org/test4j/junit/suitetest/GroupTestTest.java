package org.test4j.junit.suitetest;

import org.test4j.junit.annotations.RunGroup;
import org.test4j.junit.suitetest.GroupTest;

@RunGroup(includes = { "davey.wu" }, excludes = "exclude.test")
public class GroupTestTest extends GroupTest {
}
