package forfilter.samples;

import static org.test4j.junit.filter.SuiteType.JUNIT38_TEST_CLASSES;
import static org.test4j.junit.filter.SuiteType.JUNT4_TEST_CLASSES;

import org.junit.runner.RunWith;
import org.test4j.junit.annotations.TestPath;
import org.test4j.junit.annotations.TestPath.BaseType;
import org.test4j.junit.suitetest.suite.ClassPathSuite;

import forfilter.tests.p2.AbstractP2Test;

@RunWith(ClassPathSuite.class)
@TestPath(value = { JUNT4_TEST_CLASSES, JUNIT38_TEST_CLASSES }, baseType = @BaseType(excludes = { AbstractP2Test.class }))
public class ByExcludeBasetypeFilterSuite {

}
