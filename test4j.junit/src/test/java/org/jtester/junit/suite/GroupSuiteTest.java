package org.jtester.junit.suite;

import org.jtester.junit.annotations.RunGroup;
import org.jtester.junit.suite.GroupSuite;

@RunGroup(includes = { "davey.wu" }, excludes = "exclude.test")
public class GroupSuiteTest extends GroupSuite {
}
