package org.jtester.datafilling.strategies;

import java.util.Calendar;

import org.jtester.datafilling.exceptions.PoJoFillException;
import org.jtester.datafilling.strategy.AttributeStrategy;
import org.jtester.datafilling.utils.PodamTestUtils;

public class MyBirthdayStrategy implements AttributeStrategy<Calendar> {

	public Calendar getValue() throws PoJoFillException {

		Calendar myBirthday = PodamTestUtils.getMyBirthday();

		return myBirthday;
	}

}
