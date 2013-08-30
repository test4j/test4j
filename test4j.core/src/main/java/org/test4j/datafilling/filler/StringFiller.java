package org.test4j.datafilling.filler;

import java.lang.annotation.Annotation;

import org.test4j.datafilling.Filler;
import org.test4j.datafilling.annotations.FillString;
import org.test4j.datafilling.common.AttributeInfo;
import org.test4j.datafilling.strategy.DataFactory;

public class StringFiller extends Filler {

    public StringFiller(DataFactory strategy) {
        super(strategy, null);
    }

    private FillString getFilling(AttributeInfo attribute) {
        for (Annotation annotation : attribute.getAttrAnnotations()) {
            if (!FillString.class.isAssignableFrom(annotation.getClass())) {
                continue;
            }
            FillString filling = (FillString) annotation;
            return filling;
        }
        return null;
    }

    /**
     * It creates and returns a String value, eventually customised by
     * annotations
     * 
     * @param attributeMetadata
     * @return a String value, eventually customised by annotations
     * @throws Exception
     */
    public String fillingString(AttributeInfo attribute) throws Exception {
        FillString filling = this.getFilling(attribute);

        if (filling == null) {
            return strategy.getStringValue(null);
        } else {
            if (filling.value() != null && filling.value().length() > 0) {
                return filling.value();
            } else {
                return strategy.getStringOfLength(filling.length(), null);
            }
        }
    }
}
