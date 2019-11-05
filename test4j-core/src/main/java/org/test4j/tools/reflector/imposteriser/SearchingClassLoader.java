package org.test4j.tools.reflector.imposteriser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static java.lang.Thread.currentThread;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SearchingClassLoader extends ClassLoader {
	private final ClassLoader nextToSearch;

	public SearchingClassLoader(ClassLoader parent, ClassLoader nextToSearch) {
		super(parent);
		this.nextToSearch = nextToSearch;
	}

	public static ClassLoader combine(ClassLoader... parentLoaders) {
		return combine(Arrays.asList(parentLoaders));
	}

	public static ClassLoader combine(List<ClassLoader> parentLoaders) {
		ClassLoader loader = parentLoaders.get(parentLoaders.size() - 1);

		for (int i = parentLoaders.size() - 2; i >= 0; i--) {
			loader = new SearchingClassLoader(parentLoaders.get(i), loader);
		}

		return loader;
	}

	public static ClassLoader combineLoadersOf(Class... classes) {
		return combineLoadersOf(classes[0], classes);
	}

	public static ClassLoader combineLoadersOf(Class first, Class... others) {
		List<ClassLoader> loaders = new ArrayList<ClassLoader>();

		addIfNewElement(loaders, first.getClassLoader());
		for (Class c : others) {
			addIfNewElement(loaders, c.getClassLoader());
		}

		// To support Eclipse Plug-in tests.
		// In an Eclipse plug-in, jMock itself will not be on the system class
		// loader
		// but in the class loader of the plug-in.
		//
		// Note: I've been unable to reproduce the error in jMock's test suite.
		addIfNewElement(loaders, SearchingClassLoader.class.getClassLoader());

		// To support the Maven Surefire plugin.
		// Note: I've been unable to reproduce the error in jMock's test suite.
		addIfNewElement(loaders, currentThread().getContextClassLoader());

		addIfNewElement(loaders, ClassLoader.getSystemClassLoader());

		return combine(loaders);
	}

	private static void addIfNewElement(List<ClassLoader> loaders, ClassLoader c) {
		if (c != null && !loaders.contains(c)) {
			loaders.add(c);
		}
	}

	@Override
	protected Class findClass(String name) throws ClassNotFoundException {
		if (nextToSearch != null) {
			return nextToSearch.loadClass(name);
		} else {
			return super.findClass(name); // will throw ClassNotFoundException
		}
	}
}
