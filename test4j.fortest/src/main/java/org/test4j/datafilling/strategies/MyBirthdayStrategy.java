package org.test4j.datafilling.strategies;

import java.util.Calendar;

import org.test4j.datafilling.exceptions.PoJoFillException;
import org.test4j.datafilling.strategy.AttributeStrategy;
import org.test4j.datafilling.utils.PodamTestUtils;

public class MyBirthdayStrategy implements AttributeStrategy<Calendar> {

	public Calendar getValue() throws PoJoFillException {

		Calendar myBirthday = PodamTestUtils.getMyBirthday();

		return myBirthday;
	}

}
