package org.jtester.datafilling.filler;

import org.jtester.datafilling.Filler;
import org.jtester.datafilling.common.AttributeInfo;
import org.jtester.datafilling.exceptions.PoJoFillException;
import org.jtester.datafilling.filler.primitive.BooleanFiller;
import org.jtester.datafilling.filler.primitive.ByteFiller;
import org.jtester.datafilling.filler.primitive.CharacterFiller;
import org.jtester.datafilling.filler.primitive.DoubleFiller;
import org.jtester.datafilling.filler.primitive.FloatFiller;
import org.jtester.datafilling.filler.primitive.IntegerFiller;
import org.jtester.datafilling.filler.primitive.LongFiller;
import org.jtester.datafilling.filler.primitive.ShortFiller;
import org.jtester.datafilling.strategy.DataFactory;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class PrimitiveFiller extends Filler {

	public PrimitiveFiller(DataFactory strategy) {
		super(strategy, null);
	}

	/**
	 * It resolves and returns the primitive value depending on the type
	 * 
	 * 
	 * @param primitiveClass
	 *            The primitive type class
	 * @param attributeMetadata
	 * @return the primitive value depending on the type
	 * 
	 */
	public <T> T fillingPrimitive(AttributeInfo attribute) {
		PrimitiveFiller filler = this.getPrimitiveFiller(attribute);

		Object retValue = filler.fillWith(attribute);
		return (T) retValue;
	}

	protected Object fillWith(AttributeInfo attribute) {
		throw new RuntimeException("implemented by sub class.");
	}

	private PrimitiveFiller getPrimitiveFiller(AttributeInfo primitiveAttribute) {
		Class primitiveClass = primitiveAttribute.getAttrClaz();
		if (primitiveClass.equals(int.class) || primitiveClass.equals(Integer.class)) {
			return new IntegerFiller(strategy);
		} else if (primitiveClass.equals(long.class) || primitiveClass.equals(Long.class)) {
			return new LongFiller(strategy);
		} else if (primitiveClass.equals(float.class) || primitiveClass.equals(Float.class)) {
			return new FloatFiller(strategy);
		} else if (primitiveClass.equals(double.class) || primitiveClass.equals(Double.class)) {
			return new DoubleFiller(strategy);
		} else if (primitiveClass.equals(boolean.class) || primitiveClass.equals(Boolean.class)) {
			return new BooleanFiller(strategy);
		} else if (primitiveClass.equals(byte.class) || primitiveClass.equals(Byte.class)) {
			return new ByteFiller(strategy);
		} else if (primitiveClass.equals(short.class) || primitiveClass.equals(Short.class)) {
			return new ShortFiller(strategy);
		} else if (primitiveClass.equals(char.class) || primitiveClass.equals(Character.class)) {
			return new CharacterFiller(strategy);
		} else {
			throw new PoJoFillException(
					"the class must be a primitive, but actual is " + primitiveClass == null ? "<null>"
							: primitiveClass.getName());
		}
	}
}
