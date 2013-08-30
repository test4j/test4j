package org.jtester.tools.exception;

import java.util.Iterator;

import org.jtester.testng.JTester;
import org.jtester.tools.exception.MultipleException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("rawtypes")
public class MultipleExceptionTest extends JTester {

	@Test(dataProvider = "dataGetMultipleException")
	public void testGetMultipleException(Throwable[] errors, boolean isNull) {
		MultipleException exception = MultipleException.getMultipleException(errors);
		want.bool(exception == null).is(isNull);
	}

	@DataProvider
	public Iterator dataGetMultipleException() {
		final Exception caused = new RuntimeException();
		return new DataIterator() {
			{
				data(new Throwable[] {}, true);
				data(new Throwable[] { null }, true);
				data(new Throwable[] { null, null, null, null }, true);
				data(new Throwable[] { caused }, false);
				data(new Throwable[] { caused, null, null, null }, false);
				data(new Throwable[] { null, caused, null, null }, false);
				data(new Throwable[] { null, null, null, caused }, false);
			}
		};
	}

	public void testGetMultipleException() {
		final Exception err1 = new RuntimeException("my error1");
		final Exception err2 = new RuntimeException("my error2");
		final Exception err3 = new RuntimeException("my error3");
		MultipleException exception = MultipleException.getMultipleException(new Throwable[] { err1, err2, err3 });
		want.string(exception.getMessage()).containsInOrder("my error1", "my error2", "my error3");
	}
}
