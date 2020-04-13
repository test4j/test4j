package org.test4j.hamcrest.iassert.interal;

import org.hamcrest.BaseMatcher;
import org.test4j.hamcrest.matcher.property.ReflectionEqualMatcher;
import org.test4j.hamcrest.matcher.property.reflection.EqMode;
import org.test4j.tools.commons.ListHelper;
import org.test4j.tools.datagen.IDataMap;

import java.util.List;

public class MatcherBuilder {
    public static BaseMatcher listEqMapMatcher(IDataMap expected, EqMode... modes) {
        List _modes = ListHelper.toList(modes);
        if (!_modes.contains(EqMode.IGNORE_DEFAULTS)) {
            _modes.add(EqMode.IGNORE_DEFAULTS);
        }
        EqMode[] arr = (EqMode[]) _modes.toArray(new EqMode[0]);
        return new ReflectionEqualMatcher(expected, arr);
    }
}
