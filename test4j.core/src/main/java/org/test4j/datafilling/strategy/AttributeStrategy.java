package org.test4j.datafilling.strategy;

import org.test4j.datafilling.exceptions.PoJoFillException;

/**
 * Generic contract for attribute-level data provider strategies.
 */
public interface AttributeStrategy<T> {

	/**
	 * It returns a value of the given type
	 * 
	 * @return A value of the given type
	 * 
	 * @throws PoJoFillException
	 *             If an exception occurred while assigning the value specified
	 *             by this strategy
	 */
	public T getValue() throws PoJoFillException;
}
