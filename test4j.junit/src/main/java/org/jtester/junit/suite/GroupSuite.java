package org.jtester.junit.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Groups.class)
@Suite.SuiteClasses({ AllTestSuite.class })
public abstract class GroupSuite {

}
