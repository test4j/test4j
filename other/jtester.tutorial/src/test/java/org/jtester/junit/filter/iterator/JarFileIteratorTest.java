package org.jtester.junit.filter.iterator;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import org.jtester.junit.annotations.Group;
import org.jtester.junit.filter.iterator.JarFileIterator;
import org.junit.Test;
@Group("common")
public class JarFileIteratorTest {

	@Test
	public void recursiveRoot() throws Exception {
		Iterator<String> i = new JarFileIterator(new File("lib/mytests.jar"));
		assertTrue(i.hasNext());
		assertNotNull(i.next()); // Manifest-File
		assertNotNull(i.next());
		assertNotNull(i.next());
		assertNotNull(i.next());
		assertNotNull(i.next());
		assertFalse(i.hasNext());
	}
}
