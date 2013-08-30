package org.jtester.datafilling.strategies;

import org.jtester.datafilling.exceptions.PoJoFillException;
import org.jtester.datafilling.model.OneDimensionalTestPojo;
import org.jtester.datafilling.strategy.AttributeStrategy;

public class WrongTypeStrategy implements AttributeStrategy<OneDimensionalTestPojo> {

	public OneDimensionalTestPojo getValue() throws PoJoFillException {

		return null;
	}

}
