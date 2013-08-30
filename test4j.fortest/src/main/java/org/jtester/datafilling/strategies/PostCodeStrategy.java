package org.jtester.datafilling.strategies;

import org.jtester.datafilling.exceptions.PoJoFillException;
import org.jtester.datafilling.strategy.AttributeStrategy;
import org.jtester.datafilling.utils.FillDataTestConstants;

public class PostCodeStrategy implements AttributeStrategy<String> {

	public String getValue() throws PoJoFillException {
		return FillDataTestConstants.POST_CODE;
	}

}
