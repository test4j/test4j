package org.jtester.module.dbfit.fixture.dto;

import org.jtester.module.dbfit.utility.FitRunner;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = "jtester")
public class DtoPropertyFixtureTest extends JTester {
	@Test
	public void findMethod_String() {
		FitRunner.runFit(DtoPropertyFixtureTest.class, "testMethod_String.wiki");
	}

	@Test
	public void findMethod_Integer() {
		FitRunner.runFit(DtoPropertyFixtureTest.class, "testMethod_Integer.wiki");
	}

	@Test
	public void testMethod_dto() {
		FitRunner.runFit(DtoPropertyFixtureTest.class, "testMethod_dto.wiki");
	}

	@Test
	public void testMethod_dto_implicateParaClazz() {
		FitRunner.runFit(TestedDtoPropFixture.class, "testMethod_dto_implicateParaClazz.wiki");
	}

	@Test
	public void testMethod_SingleValue() {
		FitRunner.runFit(DtoPropertyFixtureTest.class, "testMethod_SingleValue.wiki");
	}

	@Test
	public void insertUser_SpringWrapped() {
		FitRunner.runFit(DtoPropertyFixtureTest.class, "insertUser_SpringWrapped.wiki");
	}

	@Test
	public void insertUser_SpringWrapped2() {
		FitRunner.runFit(DtoPropertyFixtureTest.class, "insertUser_SpringWrapped2.wiki");
	}
}
