package org.jtester.hamcrest.matcher.property.report;

import org.jtester.hamcrest.matcher.property.difference.Difference;

/**
 * An interface for classes that can create a string representation of a
 * Difference.
 * 
 */
public interface DifferenceView {

	/**
	 * Creates a string representation of the given difference tree.
	 * 
	 * @param difference
	 *            The root difference, not null
	 * @return The string representation, not null
	 */
	String createView(Difference difference);

}
