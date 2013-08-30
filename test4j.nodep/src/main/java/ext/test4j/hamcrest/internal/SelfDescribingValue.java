package ext.test4j.hamcrest.internal;

import ext.test4j.hamcrest.Description;
import ext.test4j.hamcrest.SelfDescribing;

public class SelfDescribingValue<T> implements SelfDescribing {
    private T value;
    
    public SelfDescribingValue(T value) {
        this.value = value;
    }

    public void describeTo(Description description) {
        description.appendValue(value);
    }
}
