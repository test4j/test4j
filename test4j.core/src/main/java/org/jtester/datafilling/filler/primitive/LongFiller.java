package org.jtester.datafilling.filler.primitive;

import java.lang.annotation.Annotation;

import org.jtester.datafilling.annotations.FillLong;
import org.jtester.datafilling.common.AttributeInfo;
import org.jtester.datafilling.filler.PrimitiveFiller;
import org.jtester.datafilling.strategy.DataFactory;
import org.jtester.module.core.utility.MessageHelper;

public class LongFiller extends PrimitiveFiller {
	public LongFiller(DataFactory strategy) {
		super(strategy);
	}

	private FillLong getFilling(AttributeInfo attribute) {
		for (Annotation annotation : attribute.getAttrAnnotations()) {
			if (FillLong.class.isAssignableFrom(annotation.getClass())) {
				final FillLong filling = (FillLong) annotation;
				return filling;
			}
		}
		return null;
	}

	@Override
	public Long fillWith(AttributeInfo attribute) {
		FillLong filling = this.getFilling(attribute);
		if (filling == null) {
			return strategy.getLong(null);
		}
		Long value = getLongValueWithinRange(filling, null);
		if (value == null) {
			value = strategy.getLong(null);
		}
		return value;
	}

	/**
	 * Returns either a customised long value if a {@link FillLong}
	 * annotation was provided or a random long if this was not the case
	 * 
	 * @param annotations
	 *            The list of annotations for the int attribute
	 * @param attributeMetadata
	 * 
	 * @return Either a customised long value if a {@link FillLong}
	 *         annotation was provided or a random long if this was not the case
	 * 
	 * @throws IllegalArgumentException
	 *             If it was not possible to convert
	 *             {@link FillLong#value()} to a Long
	 */
	private Long getLongValueWithinRange(FillLong filling, AttributeInfo attribute) {
		Long retValue = null;

		String numValueStr = filling.value();
		if (null != numValueStr && !"".equals(numValueStr)) {
			try {
				retValue = Long.valueOf(numValueStr);
			} catch (NumberFormatException nfe) {
				String errMsg = "The annotation value: " + numValueStr
						+ " could not be converted to a Long. An exception will be thrown.";
				MessageHelper.error(errMsg);
				throw new IllegalArgumentException(errMsg, nfe);
			}
		} else {
			long minValue = filling.min();
			long maxValue = filling.max();

			// Sanity check
			if (minValue > maxValue) {
				maxValue = minValue;
			}
			retValue = strategy.getLongInRange(minValue, maxValue, attribute);
		}

		return retValue;
	}
}
