package org.test4j.datafilling;

import junit.framework.Assert;

import org.junit.Test;
import org.test4j.datafilling.Filler;
import org.test4j.datafilling.model.dto.A;
import org.test4j.datafilling.model.dto.B;
import org.test4j.datafilling.model.dto.ConcreteBusinessObject;
import org.test4j.module.ICore;

public class FillerTest implements ICore {

	@Test
	public void testFilling() throws Exception {
		A pojo = Filler.filling(A.class);
		want.object(pojo).notNull();

		B b = pojo.getB();
		Assert.assertNotNull("The B object cannot be null!", b);
		Assert.assertNotNull("The Map object within B cannot be null!", b.getCustomValue());
	}

	@Test
	public void testFilling_ValidateDtoInstantiation() {
		ConcreteBusinessObject pojo = Filler.filling(ConcreteBusinessObject.class);
		Assert.assertNotNull("The created POJO cannot be null!", pojo);
	}
}
