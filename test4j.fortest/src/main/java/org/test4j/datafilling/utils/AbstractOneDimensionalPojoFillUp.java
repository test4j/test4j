package org.test4j.datafilling.utils;

import org.test4j.datafilling.FillUp;
import org.test4j.datafilling.annotations.FillInteger;
import org.test4j.datafilling.model.AbstractOneDimensionalPojo;

public class AbstractOneDimensionalPojoFillUp extends FillUp<AbstractOneDimensionalPojo> {
	@FillInteger(max = 10)
	private int parentIntField;
}
