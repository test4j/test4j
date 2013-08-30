package org.jtester.datafilling.strategies;

import org.jtester.datafilling.utils.FillDataTestConstants;
import org.test4j.datafilling.exceptions.PoJoFillException;
import org.test4j.datafilling.strategy.AttributeStrategy;

public class PostCodeStrategy implements AttributeStrategy<String> {

	public String getValue() throws PoJoFillException {
		return FillDataTestConstants.POST_CODE;
	}

}
