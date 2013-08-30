package org.test4j.junit.suite;

import org.test4j.junit.annotations.RunGroup;
import org.test4j.junit.suite.GroupSuite;

@RunGroup(includes = { "davey.wu" }, excludes = "exclude.test")
public class GroupSuiteTest extends GroupSuite {
}
