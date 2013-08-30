package org.jtester.module.dbfit.fixture;

import org.jtester.module.dbfit.annotations.DbFit;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = "jtester")
public class DatabaseFixtureTest extends JTester {
	@DbFit(when = "DatabaseFixtureTest.execute_success.when.wiki")
	public void execute() {
	}

	@DbFit(when = "DatabaseFixtureTest.delete.when.wiki", then = "DatabaseFixtureTest.delete.then.wiki")
	public void delete() {

	}

	@DbFit(when = "DatabaseFixtureTest.execute_bug_v090.when.wiki")
	public void execute_bug_v090() {

	}

	@DbFit(when = "DatabaseFixtureTest.setDateTimeFormat.when.wiki")
	public void setDateTimeFormat() {

	}

	@DbFit(when = "DatabaseFixtureTest.userAtDateVar.wiki")
	public void userAtDateVar() {

	}

	@DbFit(when = "DatabaseFixtureTest.storeQuery.wiki")
	public void storeQuery() {

	}

	@DbFit(when = "DatabaseFixtureTest.insertSpace.wiki")
	public void insertSpace() {

	}
}
