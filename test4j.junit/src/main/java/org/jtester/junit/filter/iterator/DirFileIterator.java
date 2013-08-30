package org.jtester.junit.filter.iterator;

import java.io.File;
import java.util.*;

/**
 * This class provides an iterator over all file names in a directory and its
 * subdirectories. The filenames are given relative to the root. Directories are
 * not considered to be files.
 */
public class DirFileIterator implements Iterator<String>, Iterable<String> {
	private List<String> allClazFiles = new ArrayList<String>();

	public DirFileIterator(File root) {
		int prefixLength = root.getAbsolutePath().length() + 1;
		if (root.isFile()) {
			throw new RuntimeException("the root dir can't be a file.");
		} else {
			recursivedSearch(allClazFiles, root, prefixLength);
		}
	}

	/**
	 * 递归查找所有的文件
	 * 
	 * @param allClazFiles
	 * @param dir
	 * @param prefixLength
	 */
	private static void recursivedSearch(List<String> allClazFiles, File dir, int prefixLength) {
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		for (File subFile : files) {
			if (subFile.isFile()) {
				String fileName = subFile.getAbsolutePath();
				if (fileName.endsWith(".class")) {
					allClazFiles.add(fileName.substring(prefixLength));
				}
			} else {
				recursivedSearch(allClazFiles, subFile, prefixLength);
			}
		}
	}

	private int index = 0;

	public boolean hasNext() {
		return index < this.allClazFiles.size();
	}

	public String next() {
		if (hasNext()) {
			String next = this.allClazFiles.get(index);
			index++;
			return next;
		} else {
			throw new NoSuchElementException();
		}
	}

	public void remove() {
		throw new RuntimeException("Not implemented");
	}

	public Iterator<String> iterator() {
		return this;
	}
}
