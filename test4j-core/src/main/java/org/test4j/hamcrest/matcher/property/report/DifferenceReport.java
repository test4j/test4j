package org.test4j.hamcrest.matcher.property.report;

import org.test4j.hamcrest.matcher.property.difference.Difference;

/**
 * Creates a report of the given differences.
 */
public interface DifferenceReport {

	/**
	 * Creates a report.
	 * 
	 * @param difference
	 *            The difference to output, null for a match
	 * @return The report, not null
	 */
	public String createReport(Difference difference);

}
