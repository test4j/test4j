package org.jtester.datafilling.filler.primitive;

import java.lang.annotation.Annotation;

import org.jtester.datafilling.annotations.FillFloat;
import org.jtester.datafilling.common.AttributeInfo;
import org.jtester.datafilling.filler.PrimitiveFiller;
import org.jtester.datafilling.strategy.DataFactory;
import org.jtester.module.core.utility.MessageHelper;

public class FloatFiller extends PrimitiveFiller {
	public FloatFiller(DataFactory strategy) {
		super(strategy);
	}

	private FillFloat getFilling(AttributeInfo attribute) {
		for (Annotation annotation : attribute.getAttrAnnotations()) {
			if (FillFloat.class.isAssignableFrom(annotation.getClass())) {
				final FillFloat filling = (FillFloat) annotation;
				return filling;
			}
		}
		return null;
	}

	@Override
	public Float fillWith(AttributeInfo attribute) {
		FillFloat filling = this.getFilling(attribute);
		if (filling == null) {
			return strategy.getFloat(null);
		}
		Float value = getFloatValueWithinRange(filling, null);
		if (value == null) {
			value = strategy.getFloat(null);
		}
		return value;
	}

	/**
	 * Returns either a customised float value if a {@link FillFloat}
	 * annotation was provided or a random float if this was not the case
	 * 
	 * @param attributeMetadata
	 * 
	 * 
	 * @return Either a customised float value if a {@link FillFloat}
	 *         annotation was provided or a random float if this was not the
	 *         case
	 * 
	 * @throws IllegalArgumentException
	 *             If {@link FillFloat#value()} contained a value not
	 *             convertible to a Float type
	 */
	private Float getFloatValueWithinRange(FillFloat filling, AttributeInfo attribute) {
		Float retValue = null;

		String numValueStr = filling.value();
		if (null != numValueStr && !"".equals(numValueStr)) {
			try {
				retValue = Float.valueOf(numValueStr);
			} catch (NumberFormatException nfe) {
				String errMsg = "The annotation value: " + numValueStr
						+ " could not be converted to a Float. An exception will be thrown.";
				MessageHelper.error(errMsg);
				throw new IllegalArgumentException(errMsg, nfe);
			}
		} else {

			float minValue = filling.min();
			float maxValue = filling.max();

			// Sanity check
			if (minValue > maxValue) {
				maxValue = minValue;
			}
			retValue = strategy.getFloatInRange(minValue, maxValue, attribute);
		}

		return retValue;
	}
}
