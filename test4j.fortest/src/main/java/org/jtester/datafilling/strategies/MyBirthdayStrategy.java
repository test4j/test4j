package org.jtester.datafilling.strategies;

import java.util.Calendar;

import org.jtester.datafilling.utils.PodamTestUtils;
import org.test4j.datafilling.exceptions.PoJoFillException;
import org.test4j.datafilling.strategy.AttributeStrategy;

public class MyBirthdayStrategy implements AttributeStrategy<Calendar> {

	public Calendar getValue() throws PoJoFillException {

		Calendar myBirthday = PodamTestUtils.getMyBirthday();

		return myBirthday;
	}

}
