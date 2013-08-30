package org.jtester.datafilling.filler.primitive;

import java.lang.annotation.Annotation;

import org.jtester.datafilling.annotations.FillChar;
import org.jtester.datafilling.common.AttributeInfo;
import org.jtester.datafilling.filler.PrimitiveFiller;
import org.jtester.datafilling.strategy.DataFactory;

public class CharacterFiller extends PrimitiveFiller {
	public CharacterFiller(DataFactory strategy) {
		super(strategy);
	}

	private FillChar getFilling(AttributeInfo attribute) {
		for (Annotation annotation : attribute.getAttrAnnotations()) {
			if (FillChar.class.isAssignableFrom(annotation.getClass())) {
				final FillChar filling = (FillChar) annotation;
				return filling;
			}
		}
		return null;
	}

	@Override
	public Character fillWith(AttributeInfo attribute) {
		FillChar filling = this.getFilling(attribute);
		if (filling == null) {
			return strategy.getCharacter(null);
		}
		Character value = getCharacterValueWithinRange(filling, null);
		if (value == null) {
			value = strategy.getCharacter(null);
		}
		return value;
	}

	/**
	 * It creates and returns a random {@link Character} value
	 * 
	 * @param annotations
	 *            The list of annotations which might customise the return value
	 * @param attributeMetadata
	 * 
	 * @return A random {@link Character} value
	 */
	private Character getCharacterValueWithinRange(FillChar filling, AttributeInfo attribute) {
		Character retValue = null;

		char charValue = filling.value();
		if (charValue != ' ') {
			retValue = charValue;
		} else {
			char minValue = filling.min();
			char maxValue = filling.max();

			// Sanity check
			if (minValue > maxValue) {
				maxValue = minValue;
			}
			retValue = strategy.getCharacterInRange(minValue, maxValue, attribute);
		}

		return retValue;
	}
}
