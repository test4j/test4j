package forfilter.tests.p2;

import org.junit.*;

public abstract class AbstractP2Test {
	@Test
	public void testSuccess() {
	}

	@Test(expected = AssertionError.class)
	public void testFailure() {
		Assert.fail();
	}
}
