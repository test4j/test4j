package org.jtester.junit.filter.iterator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.jtester.hamcrest.matcher.string.StringMode;
import org.jtester.junit.filter.iterator.DirFileIterator;
import org.jtester.module.ICore;
import org.junit.Test;

public class DirFileIteratorTest implements ICore {

	protected Iterator<String> createFileIterator(String path) {
		return new DirFileIterator(new File("./target/test-classes/forfilter/tests/" + path));
	}

	private void assertNextFilename(Iterator<String> i, String expectedName) {
		want.bool(i.hasNext()).isEqualTo(true);
		String filename = i.next();
		want.string(filename).isEqualTo(expectedName, StringMode.SameAsSlash);
	}

	@Test(expected = NoSuchElementException.class)
	public void emptyRoot() {
		Iterator<String> i = createFileIterator("p/emptysubdir");
		want.bool(i.hasNext()).isEqualTo(false);
		i.next();
	}

	@Test(expected = NoSuchElementException.class)
	public void rootWithEmptySubDirectory() {
		Iterator<String> i = createFileIterator("p");
		want.bool(i.hasNext()).isEqualTo(false);
		i.next();
	}

	@Test(expected = NoSuchElementException.class)
	public void threeFilesInRoot() {
		Iterator<String> i = createFileIterator("p1");
		assertNextFilename(i, "P1NoTest$InnerTest.class");
		assertNextFilename(i, "P1NoTest.class");
		assertNextFilename(i, "P1Test.class");
		assertFalse(i.hasNext());
		i.next();
	}

	@Test
	public void recursiveRoot() {
		Iterator<String> i = createFileIterator("");
		assertTrue(i.hasNext());
		assertNextFilename(i, "ju38/JU38AbstractTest.class");
		assertNextFilename(i, "ju38/JU38ConcreteTest.class");
		assertNextFilename(i, "ju38/JU38TestWithoutBase.class");
		assertNextFilename(i, "p1/P1NoTest$InnerTest.class");
		assertNextFilename(i, "p1/P1NoTest.class");
		assertNextFilename(i, "p1/P1Test.class");
		assertNextFilename(i, "p2/AbstractP2Test.class");
		assertNextFilename(i, "p2/ConcreteP2Test.class");
		assertFalse(i.hasNext());
	}
}
