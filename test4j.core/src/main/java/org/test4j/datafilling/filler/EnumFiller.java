package org.test4j.datafilling.filler;

import org.test4j.datafilling.Filler;
import org.test4j.datafilling.common.AttributeInfo;
import org.test4j.datafilling.strategy.DataFactory;

public class EnumFiller extends Filler {

    public EnumFiller(DataFactory strategy) {
        super(strategy, null);
    }

    public Object fillingEnum(AttributeInfo attribute) {
        int enumConstantsLength = attribute.getEnumConstants().length;
        if (enumConstantsLength > 0) {
            int enumIndex = getEnumIndex(attribute, enumConstantsLength);
            return attribute.getEnumConstants()[enumIndex];
        } else {
            return null;
        }
    }

    private int getEnumIndex(AttributeInfo attribute, int enumConstantsLength) {
        int enumIndex = strategy.getIntegerInRange(0, enumConstantsLength, attribute) % enumConstantsLength;
        return enumIndex;
    }
}
