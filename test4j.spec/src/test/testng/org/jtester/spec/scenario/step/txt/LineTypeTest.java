package org.jtester.spec.scenario.step.txt;

import static org.jtester.spec.scenario.step.txt.LineType.*;

import java.util.Iterator;

import org.jtester.spec.scenario.step.txt.LineType;
import org.jtester.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("rawtypes")
public class LineTypeTest extends JTester {

	@Test(dataProvider = "dataForGetSurfixText")
	public void testGetSurfixText(String line, LineType lineType, String surfix) {
		LineType type = LineType.getLineType(line);
		want.object(type).isEqualTo(lineType);
		String value = type.getSurfixText(line);
		want.string(value).isEqualTo(surfix);
	}

	@DataProvider
	public Iterator dataForGetSurfixText() {
		return new DataIterator() {
			{
				data("Given test method", Given, "test method");
				data("Given\ttest method", Given, "test method");
				data("Giventest method", TextLine, "Giventest method");

				data("SkipGiven test method", SkipGiven, "test method");
				data("SkipGiven\ttest method", SkipGiven, "test method");
				data("SkipGiventest method", TextLine, "SkipGiventest method");

				data("When test method", When, "test method");
				data("When\ttest method", When, "test method");
				data("Whentest method", TextLine, "Whentest method");

				data("SkipWhen test method", SkipWhen, "test method");
				data("SkipWhen\ttest method", SkipWhen, "test method");
				data("SkipWhentest method", TextLine, "SkipWhentest method");

				data("Then test method", Then, "test method");
				data("Then\ttest method", Then, "test method");
				data("Thentest method", TextLine, "Thentest method");

				data("SkipThen test method", SkipThen, "test method");
				data("SkipThen\ttest method", SkipThen, "test method");
				data("SkipThentest method", TextLine, "SkipThentest method");

				data("Scenario description", Scenario, "description");
				data("Scenario\tdescription", Scenario, "description");
				data("Scenariodescription", TextLine, "Scenariodescription");

				data("SkipScenario description", SkipScenario, "description");
				data("SkipScenario\tdescription", SkipScenario, "description");
				data("SkipScenariodescription", TextLine, "SkipScenariodescription");

				data("test method", TextLine, "test method");
			}
		};
	}
}
