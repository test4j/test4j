package org.jtester.datafilling.filler.primitive;

import java.lang.annotation.Annotation;

import org.jtester.datafilling.annotations.FillDouble;
import org.jtester.datafilling.common.AttributeInfo;
import org.jtester.datafilling.filler.PrimitiveFiller;
import org.jtester.datafilling.strategy.DataFactory;
import org.jtester.module.core.utility.MessageHelper;

public class DoubleFiller extends PrimitiveFiller {
	public DoubleFiller(DataFactory strategy) {
		super(strategy);
	}

	private FillDouble getFilling(AttributeInfo attribute) {
		for (Annotation annotation : attribute.getAttrAnnotations()) {
			if (FillDouble.class.isAssignableFrom(annotation.getClass())) {
				final FillDouble filling = (FillDouble) annotation;
				return filling;
			}
		}
		return null;
	}

	@Override
	public Double fillWith(AttributeInfo attribute) {
		FillDouble filling = this.getFilling(attribute);
		if (filling == null) {
			return strategy.getDouble(null);
		}
		Double value = getDoubleValueWithinRange(filling, null);
		if (value == null) {
			value = strategy.getDouble(null);
		}
		return value;
	}

	/**
	 * It creates and returns a random {@link Double} value
	 * 
	 * @param annotations
	 *            The list of annotations which might customise the return value
	 * @param attributeMetadata
	 * 
	 * 
	 * @return a random {@link Double} value
	 */
	private Double getDoubleValueWithinRange(FillDouble filling, AttributeInfo attribute) {
		Double retValue = null;

		String numValueStr = filling.value();
		if (null != numValueStr && !"".equals(numValueStr)) {

			try {
				retValue = Double.valueOf(numValueStr);
			} catch (NumberFormatException nfe) {
				String errMsg = "The annotation value: " + numValueStr
						+ " could not be converted to a Double. An exception will be thrown.";
				MessageHelper.error(errMsg);
				throw new IllegalArgumentException(errMsg, nfe);
			}
		} else {
			double minValue = filling.min();
			double maxValue = filling.max();

			if (minValue > maxValue) {
				maxValue = minValue;
			}
			retValue = strategy.getDoubleInRange(minValue, maxValue, attribute);
		}
		return retValue;
	}
}
