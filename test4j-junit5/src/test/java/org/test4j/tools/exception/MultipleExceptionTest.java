package org.test4j.tools.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.Test4J;
import org.test4j.exception.MultipleException;

import org.test4j.tools.datagen.DataProvider;

import java.util.Iterator;

@SuppressWarnings("rawtypes")
public class MultipleExceptionTest implements Test4J {

    @ParameterizedTest
    @MethodSource("dataGetMultipleException")
    public void testGetMultipleException(Throwable[] errors, boolean isNull) {
        MultipleException exception = MultipleException.getMultipleException(errors);
        want.bool(exception == null).is(isNull);
    }

    public static Iterator dataGetMultipleException() {
        final Exception caused = new RuntimeException();
        return new DataProvider() {
            {
                data(new Throwable[]{}, true);
                data(new Throwable[]{null}, true);
                data(new Throwable[]{null, null, null, null}, true);
                data(new Throwable[]{caused}, false);
                data(new Throwable[]{caused, null, null, null}, false);
                data(new Throwable[]{null, caused, null, null}, false);
                data(new Throwable[]{null, null, null, caused}, false);
            }
        };
    }

    @Test
    public void testGetMultipleException() {
        final Exception err1 = new RuntimeException("my error1");
        final Exception err2 = new RuntimeException("my error2");
        final Exception err3 = new RuntimeException("my error3");
        MultipleException exception = MultipleException.getMultipleException(new Throwable[]{err1, err2, err3});
        want.string(exception.getMessage()).containsInOrder("my error1", "my error2", "my error3");
    }
}