package org.jtester.hamcrest.matcher.property.report;

import static org.jtester.hamcrest.matcher.property.report.DefaultDifferenceReport.MAX_LINE_SIZE;

import org.jtester.hamcrest.matcher.property.difference.Difference;

public class SimpleDifferenceView implements DifferenceView {

	private ObjectFormatter objectFormatter = new ObjectFormatter();

	/**
	 * Creates a string representation of the given difference tree.
	 * 
	 * @param difference
	 *            The root difference, not null
	 * @return The string representation, not null
	 */
	public String createView(Difference difference) {
		String expectedStr = objectFormatter.format(difference.getLeftValue());
		String actualStr = objectFormatter.format(difference.getRightValue());
		String formattedOnOneLine = formatOnOneLine(expectedStr, actualStr);
		if (AssertionError.class.getName().length() + 2 + formattedOnOneLine.length() < MAX_LINE_SIZE) {
			return formattedOnOneLine;
		} else {
			return formatOnTwoLines(expectedStr, actualStr);
		}
	}

	protected String formatOnOneLine(String expectedStr, String actualStr) {
		return new StringBuilder().append("Expected: ").append(expectedStr).append(", actual: ").append(actualStr)
				.toString();
	}

	protected String formatOnTwoLines(String expectedStr, String actualStr) {
		StringBuilder result = new StringBuilder();
		result.append("\nExpected: ").append(expectedStr);
		result.append("\n  Actual: ").append(actualStr);
		return result.toString();
	}
}
