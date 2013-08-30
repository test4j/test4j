package org.jtester.datafilling.filler.primitive;

import java.lang.annotation.Annotation;

import org.jtester.datafilling.annotations.FillShort;
import org.jtester.datafilling.common.AttributeInfo;
import org.jtester.datafilling.filler.PrimitiveFiller;
import org.jtester.datafilling.strategy.DataFactory;
import org.jtester.module.core.utility.MessageHelper;

public class ShortFiller extends PrimitiveFiller {

	public ShortFiller(DataFactory strategy) {
		super(strategy);
	}

	private FillShort getFilling(AttributeInfo attribute) {
		for (Annotation annotation : attribute.getAttrAnnotations()) {
			if (FillShort.class.isAssignableFrom(annotation.getClass())) {
				final FillShort filling = (FillShort) annotation;
				return filling;
			}
		}
		return null;
	}

	@Override
	public Short fillWith(AttributeInfo attribute) {
		FillShort filling = this.getFilling(attribute);
		if (filling == null) {
			return strategy.getShort(null);
		}
		Short value = getShortValueWithinRange(filling, null);
		if (value == null) {
			value = strategy.getShort(null);
		}
		return value;
	}

	/**
	 * It returns a random short if the attribute was annotated with
	 * {@link FillShort} or {@code null} otherwise
	 * 
	 * @param annotations
	 *            The annotations with which the attribute was annotated
	 * @param attributeMetadata
	 * 
	 * 
	 * @return A random short if the attribute was annotated with
	 *         {@link FillShort} or {@code null} otherwise
	 * @throws IllegalArgumentException
	 *             If {@link FillShort#value()} was set and its value
	 *             could not be converted to a Short type
	 */
	private Short getShortValueWithinRange(FillShort filling, AttributeInfo attribute) {
		Short retValue = null;

		String numValueStr = filling.value();
		if (null != numValueStr && !"".equals(numValueStr)) {
			try {
				retValue = Short.valueOf(numValueStr);
			} catch (NumberFormatException nfe) {
				String errMsg = "The precise value: " + numValueStr
						+ " cannot be converted to a short type. An exception will be thrown.";
				MessageHelper.error(errMsg);
				throw new IllegalArgumentException(errMsg, nfe);
			}
		} else {
			short minValue = filling.min();
			short maxValue = filling.max();

			if (minValue > maxValue) {
				maxValue = minValue;
			}
			retValue = strategy.getShortInRange(minValue, maxValue, attribute);
		}

		return retValue;
	}
}
