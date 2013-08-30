package org.jtester.datafilling.utils;

import org.jtester.datafilling.model.AbstractOneDimensionalPojo;
import org.test4j.datafilling.FillUp;
import org.test4j.datafilling.annotations.FillInteger;

public class AbstractOneDimensionalPojoFillUp extends FillUp<AbstractOneDimensionalPojo> {
	@FillInteger(max = 10)
	private int parentIntField;
}
