package org.jtester.hamcrest.matcher.property.report;

import org.jtester.hamcrest.matcher.property.difference.Difference;

/**
 * Creates a report of the given differences. This will first output the
 * differences using the default difference view. If the difference is not a
 * simple difference, this will also output the difference tree using the
 * difference tree view.
 * 
 */
public class DefaultDifferenceReport implements DifferenceReport {

	public static final int MAX_LINE_SIZE = 110;

	/**
	 * Creates a report.
	 * 
	 * @param difference
	 *            The difference to output, null for a match
	 * @return The report, not null
	 */
	public String createReport(Difference difference) {
		StringBuilder result = new StringBuilder();
		result.append(new SimpleDifferenceView().createView(difference)).append("\n\n");
		result.append("--- Found following differences ---\n");
		result.append(new DefaultDifferenceView().createView(difference));
		if (!Difference.class.equals(difference.getClass())) {
			result.append("\n--- Difference detail tree ---\n");
			result.append(new TreeDifferenceView().createView(difference));
		}
		return result.toString();
	}

	public static enum MatchType {
		NO_MATCH
	}
}