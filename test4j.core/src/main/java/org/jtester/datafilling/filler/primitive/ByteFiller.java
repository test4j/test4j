package org.jtester.datafilling.filler.primitive;

import java.lang.annotation.Annotation;

import org.jtester.datafilling.annotations.FillByte;
import org.jtester.datafilling.common.AttributeInfo;
import org.jtester.datafilling.filler.PrimitiveFiller;
import org.jtester.datafilling.strategy.DataFactory;
import org.jtester.module.core.utility.MessageHelper;

public class ByteFiller extends PrimitiveFiller {
	public ByteFiller(DataFactory strategy) {
		super(strategy);
	}

	private FillByte getFilling(AttributeInfo attribute) {
		for (Annotation annotation : attribute.getAttrAnnotations()) {
			if (FillByte.class.isAssignableFrom(annotation.getClass())) {
				final FillByte filling = (FillByte) annotation;
				return filling;
			}
		}
		return null;
	}

	@Override
	public Byte fillWith(AttributeInfo attribute) {
		FillByte filling = this.getFilling(attribute);
		if (filling == null) {
			return strategy.getByte(null);
		}
		Byte value = getByteValueWithinRange(filling, null);
		if (value == null) {
			value = strategy.getByte(null);
		}
		return value;
	}

	/**
	 * It returns a random byte if the attribute was annotated with
	 * {@link FillByte} or {@code null} otherwise
	 * 
	 * @param attributeMetadata
	 * @return A random byte if the attribute was annotated with
	 * @throws IllegalArgumentException
	 *             If the {@link FillByte#value()} value has been set and
	 *             it is not convertible to a byte type
	 */
	private Byte getByteValueWithinRange(FillByte filling, AttributeInfo attribute) {
		Byte retValue = null;

		String numValueStr = filling.value();
		if (null != numValueStr && !"".equals(numValueStr)) {
			try {
				retValue = Byte.valueOf(numValueStr);
			} catch (NumberFormatException nfe) {
				String errMsg = "The precise value: " + numValueStr
						+ " cannot be converted to a byte type. An exception will be thrown.";
				MessageHelper.error(errMsg);
				throw new IllegalArgumentException(errMsg, nfe);
			}
		} else {
			byte minValue = filling.min();
			byte maxValue = filling.max();

			if (minValue > maxValue) {
				maxValue = minValue;
			}
			retValue = strategy.getByteInRange(minValue, maxValue, attribute);
		}

		return retValue;
	}
}
