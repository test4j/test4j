package org.test4j.datafilling.filler.primitive;

import java.lang.annotation.Annotation;

import org.test4j.datafilling.annotations.FillInteger;
import org.test4j.datafilling.common.AttributeInfo;
import org.test4j.datafilling.filler.PrimitiveFiller;
import org.test4j.datafilling.strategy.DataFactory;
import org.test4j.module.core.utility.MessageHelper;

public class IntegerFiller extends PrimitiveFiller {
	public IntegerFiller(DataFactory strategy) {
		super(strategy);
	}

	private FillInteger getFilling(AttributeInfo attribute) {
		for (Annotation annotation : attribute.getAttrAnnotations()) {
			if (FillInteger.class.isAssignableFrom(annotation.getClass())) {
				final FillInteger filling = (FillInteger) annotation;
				return filling;
			}
		}
		return null;
	}

	@Override
	public Integer fillWith(AttributeInfo attribute) {
		FillInteger filling = this.getFilling(attribute);
		if (filling == null) {
			return strategy.getInteger(null);
		}
		Integer retValue = getIntegerValueWithinRange(filling, null);
		if (retValue == null) {
			retValue = strategy.getInteger(null);
		}
		return retValue;
	}

	/**
	 * Returns either a customised int value if a {@link FillInteger}
	 * annotation was provided or a random integer if this was not the case
	 * 
	 * @param annotations
	 *            The list of annotations for the int attribute
	 * @param attributeMetadata
	 * 
	 * @return Either a customised int value if a {@link FillInteger}
	 *         annotation was provided or a random integer if this was not the
	 *         case
	 * 
	 * @throws IllegalArgumentException
	 *             If it was not possible to convert the
	 *             {@link FillInteger#value()} to an Integer
	 */
	private Integer getIntegerValueWithinRange(FillInteger filling, AttributeInfo attribute) {
		Integer retValue = null;

		String numValueStr = filling.value();
		if (null != numValueStr && !"".equals(numValueStr)) {
			try {
				retValue = Integer.valueOf(numValueStr);
			} catch (NumberFormatException nfe) {
				String errMsg = "The annotation value: " + numValueStr
						+ " could not be converted to an Integer. An exception will be thrown.";
				MessageHelper.error(errMsg);
				throw new IllegalArgumentException(errMsg, nfe);
			}
		} else {
			int minValue = filling.min();
			int maxValue = filling.max();

			// Sanity check
			if (minValue > maxValue) {
				maxValue = minValue;
			}
			retValue = strategy.getIntegerInRange(minValue, maxValue, attribute);
		}

		return retValue;
	}
}
