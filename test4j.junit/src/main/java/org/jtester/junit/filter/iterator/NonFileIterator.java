package org.jtester.junit.filter.iterator;

import java.util.*;

/**
 * 没有任何文件的迭代
 * 
 * @author darui.wudr
 * 
 * @param <T>
 */
public class NonFileIterator<T> implements Iterable<T>, Iterator<T> {

	public Iterator<T> iterator() {
		return this;
	}

	public boolean hasNext() {
		return false;
	}

	public T next() {
		throw new NoSuchElementException();
	}

	public void remove() {
	}
}
