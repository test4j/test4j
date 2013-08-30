package org.jtester.datafilling.utils;

import org.jtester.datafilling.FillUp;
import org.jtester.datafilling.annotations.FillInteger;
import org.jtester.datafilling.model.AbstractOneDimensionalPojo;

public class AbstractOneDimensionalPojoFillUp extends FillUp<AbstractOneDimensionalPojo> {
	@FillInteger(max = 10)
	private int parentIntField;
}
