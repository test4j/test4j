package org.test4j.module.dbfit.utility;

import java.io.File;

import org.test4j.module.dbfit.utility.FitRunner;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = "test4j")
public class FitRunnerTest extends Test4J {
	TestedFitRunner fitRunner = new TestedFitRunner();

	@Test
	public void testPrepareFiles() {
		File file = new File(fitRunner.getFitDir() + File.separatorChar + "files/fitnesse.css");
		file.delete();
		want.file(file).unExists();
		fitRunner.prepareFiles();
		want.file(file).isExists();
	}

	@Test
	public void testRunFitTest_BlankFileName() throws Exception {
		fitRunner.runFitTest(FitRunnerTest.class, "");
	}

	@Test(expectedExceptions = RuntimeException.class)
	public void testRunFitTest_InvalidateFileName() throws Exception {
		fitRunner.runFitTest(FitRunnerTest.class, ".wiki");
	}

	@Test(expectedExceptions = RuntimeException.class)
	public void testRunFitTest_InvalidateFileName2() throws Exception {
		fitRunner.runFitTest(FitRunnerTest.class, "aa.wik");
	}

	// public void testRunExcel() {
	// fitRunner.runFitExcel(FitRunnerTest.class,
	// "D:/eclipse/workspace/ali-test4j/src/test/resources/org/test4j/module/dbfit/utility/simple query.xls");
	// }

	public static class TestedFitRunner extends FitRunner {
		public TestedFitRunner() {
			super();
		}

		public void prepareFiles() {
			super.prepareFiles();
		}
	}
}
