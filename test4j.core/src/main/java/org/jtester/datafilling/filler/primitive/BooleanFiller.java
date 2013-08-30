package org.jtester.datafilling.filler.primitive;

import java.lang.annotation.Annotation;

import org.jtester.datafilling.annotations.FillBoolean;
import org.jtester.datafilling.common.AttributeInfo;
import org.jtester.datafilling.filler.PrimitiveFiller;
import org.jtester.datafilling.strategy.DataFactory;

public class BooleanFiller extends PrimitiveFiller {

	public BooleanFiller(DataFactory strategy) {
		super(strategy);
	}

	private FillBoolean getFilling(AttributeInfo attribute) {
		for (Annotation annotation : attribute.getAttrAnnotations()) {
			if (FillBoolean.class.isAssignableFrom(annotation.getClass())) {
				final FillBoolean filling = (FillBoolean) annotation;
				return filling;
			}
		}
		return null;
	}

	/**
	 * It returns the boolean value indicated in the annotation.
	 * 
	 * @return The boolean value indicated in the annotation
	 */
	@Override
	public Boolean fillWith(AttributeInfo attribute) {
		FillBoolean filling = getFilling(attribute);
		if (filling == null) {
			return Boolean.TRUE;
		} else {
			Boolean value = filling.value();
			return value;
		}
	}
}
