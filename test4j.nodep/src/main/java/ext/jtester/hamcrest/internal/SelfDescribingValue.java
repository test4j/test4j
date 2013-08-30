package ext.jtester.hamcrest.internal;

import ext.jtester.hamcrest.Description;
import ext.jtester.hamcrest.SelfDescribing;

public class SelfDescribingValue<T> implements SelfDescribing {
    private T value;
    
    public SelfDescribingValue(T value) {
        this.value = value;
    }

    public void describeTo(Description description) {
        description.appendValue(value);
    }
}
