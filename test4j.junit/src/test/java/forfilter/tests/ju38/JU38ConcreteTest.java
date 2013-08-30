package forfilter.tests.ju38;

import org.jtester.module.ICore;

public class JU38ConcreteTest extends JU38AbstractTest implements ICore {

	public void testOk() {

	}

	public void testFailure() {
		try {
			want.fail("expected");
			throw new RuntimeException("can't be called!");
		} catch (AssertionError e) {
		}
	}
}
