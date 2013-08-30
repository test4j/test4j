package org.jtester.module.jmockit.dynamic;

import mockit.Mocked;

import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = "testng-demo")
public class CollaboratorTest extends JTester {
	@Test
	public void dynamicallyMockAClass() {
		new Expectations(Collaborator.class) {
			{
				when(new Collaborator().getValue()).thenReturn(123);
			}
		};
		// Mocked:
		Collaborator collaborator = new Collaborator();
		want.number(collaborator.getValue()).isEqualTo(123);

		// Not mocked:
		boolean ret = collaborator.simpleOperation(1, "b", null);
		want.bool(ret).isEqualTo(true);
		want.object(new Collaborator(45).value).isEqualTo(45);
	}

	@Test
	public void dynamicallyMockAnInstance() {
		final Collaborator collaborator = new Collaborator(2);

		new NonStrictExpectations(collaborator) {
			{
				when(collaborator.simpleOperation(1, "", null)).thenReturn(false);
				Collaborator.doSomething(true, "test");
			}
		};
		// Mocked:
		want.bool(collaborator.simpleOperation(1, "", null)).is(false);
		Collaborator.doSomething(false, null);

		// Not mocked:
		want.number(collaborator.getValue()).isEqualTo(2);
		want.object(new Collaborator(45).value).isEqualTo(45);
		want.object(new Collaborator().value).isEqualTo(-1);
	}

	@Test
	public void staticPartialMockAClass() {
		new Expectations() {
			@Mocked(methods = "getValue")
			Collaborator collaborator;
			{
				when(new Collaborator().getValue()).thenReturn(123);
			}
		};
		// Mocked:
		Collaborator collaborator = new Collaborator();
		want.number(collaborator.getValue()).isEqualTo(123);

		// Not mocked:
		want.bool(collaborator.simpleOperation(1, "b", null)).isEqualTo(true);
		want.object(new Collaborator(45).value).isEqualTo(45);
	}

	@Test
	public void staticPartialMockAClass2(@Mocked(methods = "getValue") Collaborator collaborator) {
		new Expectations() {
			{
				when(new Collaborator().getValue()).thenReturn(123);
			}
		};
		// Mocked:
		collaborator = new Collaborator();
		want.number(collaborator.getValue()).isEqualTo(123);

		// Not mocked:
		want.bool(collaborator.simpleOperation(1, "b", null)).isEqualTo(true);
		want.object(new Collaborator(45).value).isEqualTo(45);
	}
}
