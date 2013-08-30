package org.test4j.datafilling.strategies;

import org.test4j.datafilling.exceptions.PoJoFillException;
import org.test4j.datafilling.strategy.AttributeStrategy;
import org.test4j.datafilling.utils.FillDataTestConstants;

public class PostCodeStrategy implements AttributeStrategy<String> {

	public String getValue() throws PoJoFillException {
		return FillDataTestConstants.POST_CODE;
	}

}
