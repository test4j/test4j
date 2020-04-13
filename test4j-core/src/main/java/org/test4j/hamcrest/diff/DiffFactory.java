package org.test4j.hamcrest.diff;

import lombok.Getter;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.test4j.hamcrest.matcher.property.reflection.EqMode;
import org.test4j.tools.commons.ArrayHelper;

import java.util.Map;

/**
 * Map对象比较
 *
 * @author wudarui
 */
@Getter
public class DiffFactory extends BaseDiff {

    public DiffFactory() {
        super(true, true, true);
    }

    public DiffFactory(EqMode... modes) {
        super(modes);
    }

    public DiffFactory(boolean ignoreNull, boolean asString, boolean ignoreOrder) {
        super(ignoreNull, asString, ignoreOrder);
    }

    public DiffFactory(BaseDiff diff) {
        super(diff);
    }

    public DiffFactory(DiffByList diff, DiffMap diffMap) {
        super(diff, diffMap);
    }

    public static DiffMap diffBy(Object actual, Object expect, EqMode... modes) {
        return new DiffFactory(modes).compare("$", actual, expect);
    }

    public DiffMap diff(Object actual, Object expect) {
        return this.compare("$", actual, expect);
    }

    @Override
    public DiffMap compare(Object parentKey, Object actual, Object expect) {
        if (validateNull(parentKey, actual, expect)) {
            return this.diffMap;
        } else if (expect instanceof Matcher) {
            this.diffByMatcher(parentKey, actual, (Matcher) expect);
        } else if (ArrayHelper.isCollOrArray(actual)) {
            new DiffByList(this).compare(parentKey, actual, expect);
        } else if (expect instanceof Map) {
            new DiffByMap(this).compare(parentKey, actual, (Map) expect);
        } else {
            new DiffByReflect(this).compare(parentKey, actual, expect);
        }
        return this.diffMap;
    }

    private void diffByMatcher(Object parentKey, Object actual, Matcher matcher) {
        boolean matched = matcher.matches(actual);
        if (!matched) {
            StringDescription description = new StringDescription();
            matcher.describeTo(description);
            this.diffMap.add(parentKey, actual, description.toString());
        }
    }
}