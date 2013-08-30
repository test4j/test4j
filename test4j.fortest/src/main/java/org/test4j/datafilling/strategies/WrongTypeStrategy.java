package org.test4j.datafilling.strategies;

import org.test4j.datafilling.exceptions.PoJoFillException;
import org.test4j.datafilling.model.OneDimensionalTestPojo;
import org.test4j.datafilling.strategy.AttributeStrategy;

public class WrongTypeStrategy implements AttributeStrategy<OneDimensionalTestPojo> {

	public OneDimensionalTestPojo getValue() throws PoJoFillException {

		return null;
	}

}
