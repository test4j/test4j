/*  Copyright (c) 2000-2006 hamcrest.org
 */
package ext.jtester.hamcrest.number;

import ext.jtester.hamcrest.Description;
import ext.jtester.hamcrest.Matcher;
import ext.jtester.hamcrest.Factory;
import ext.jtester.hamcrest.TypeSafeMatcher;


/**
 * Is the value a number equal to a value within some range of
 * acceptable error?
 */
public class IsCloseTo extends TypeSafeMatcher<Double> {
    private final double delta;
    private final double value;

    public IsCloseTo(double value, double error) {
        this.delta = error;
        this.value = value;
    }

    @Override
    public boolean matchesSafely(Double item) {
        return actualDelta(item) <= 0.0;
    }

    @Override
    public void describeMismatchSafely(Double item, Description mismatchDescription) {
      mismatchDescription.appendValue(item)
                         .appendText(" differed by ")
                         .appendValue(actualDelta(item));
    }

    public void describeTo(Description description) {
        description.appendText("a numeric value within ")
                .appendValue(delta)
                .appendText(" of ")
                .appendValue(value);
    }

    private double actualDelta(Double item) {
      return (Math.abs((item - value)) - delta);
    }


    @Factory
    public static Matcher<Double> closeTo(double operand, double error) {
        return new IsCloseTo(operand, error);
    }

}
