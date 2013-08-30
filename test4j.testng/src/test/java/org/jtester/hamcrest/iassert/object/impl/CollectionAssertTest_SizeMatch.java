package org.jtester.hamcrest.iassert.object.impl;

import java.util.Arrays;

import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "assertion" })
public class CollectionAssertTest_SizeMatch extends JTester {

	@Test
	public void sizeIs() {
		want.collection(Arrays.asList(1, 2, 4)).sizeIs(3);
	}

	@Test
	public void sizeIs_1() {
		want.array(new int[] { 1, 2, 3 }).sizeEq(3);
	}

	@Test(expectedExceptions = { AssertionError.class })
	public void sizeIs_2() {
		want.collection(Arrays.asList(1, 2, 4)).sizeIs(2);
	}

	@Test(expectedExceptions = { AssertionError.class })
	public void sizeIs_3() {
		want.collection(Arrays.asList(1, 2, 4)).sizeIs(4);
	}

	@Test
	public void sizeGe() {
		want.collection(Arrays.asList(1, 2, 4)).sizeGe(2);
	}

	@Test
	public void sizeGe_2() {
		want.collection(Arrays.asList(1, 2, 4)).sizeGe(3);
	}

	@Test(expectedExceptions = { AssertionError.class })
	public void sizeGe_3() {
		want.collection(Arrays.asList(1, 2, 4)).sizeGe(4);
	}

	@Test(expectedExceptions = { AssertionError.class })
	public void sizeGt() {
		want.collection(Arrays.asList(1, 2, 4)).sizeGt(3);
	}

	@Test
	public void sizeGt_2() {
		want.collection(Arrays.asList(1, 2, 4)).sizeGt(2);
	}

	@Test(expectedExceptions = { AssertionError.class })
	public void sizeGt_3() {
		want.collection(Arrays.asList(1, 2, 4)).sizeGt(4);
	}

	@Test
	public void sizeLe() {
		want.collection(Arrays.asList(1, 2, 4)).sizeLe(4);
	}

	@Test
	public void sizeLe_2() {
		want.collection(Arrays.asList(1, 2, 4)).sizeLe(3);
	}

	@Test(expectedExceptions = { AssertionError.class })
	public void sizeLe_3() {
		want.collection(Arrays.asList(1, 2, 4)).sizeLe(2);
	}

	@Test
	public void sizeLt() {
		want.collection(Arrays.asList(1, 2, 4)).sizeLt(4);
	}

	@Test(expectedExceptions = { AssertionError.class })
	public void sizeLt_2() {
		want.collection(Arrays.asList(1, 2, 4)).sizeLt(3);
	}

	@Test(expectedExceptions = { AssertionError.class })
	public void sizeLt_3() {
		want.collection(Arrays.asList(1, 2, 4)).sizeLt(2);
	}

	@Test
	public void sizeNe() {
		want.array(new String[] { "", null, "ddd" }).sizeNe(4);
	}

	@Test(expectedExceptions = { AssertionError.class })
	public void sizeNe_2() {
		want.array(new String[] { "", null, "ddd" }).sizeNe(3);
	}

	@Test
	public void sizeNe_3() {
		want.array(new String[] { "", null, "ddd" }).sizeNe(2);
	}
}
