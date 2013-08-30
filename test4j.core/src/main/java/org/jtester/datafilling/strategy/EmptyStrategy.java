package org.jtester.datafilling.strategy;

import org.jtester.datafilling.exceptions.PoJoFillException;

/**
 * A default Object strategy, just to provide a default to
 * {@link CollectionGenerator#collectionElementStrategy()}.
 */
public class EmptyStrategy implements AttributeStrategy<Object> {

	/**
	 * {@inheritDoc}
	 */
	public Object getValue() throws PoJoFillException {
		return new Object();
	}
}
