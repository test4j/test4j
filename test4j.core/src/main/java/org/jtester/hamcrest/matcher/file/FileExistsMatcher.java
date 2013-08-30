package org.jtester.hamcrest.matcher.file;

import java.io.File;

import ext.jtester.hamcrest.BaseMatcher;
import ext.jtester.hamcrest.Description;

public class FileExistsMatcher extends BaseMatcher<File> {
	private File expected;

	private FileExistsMatcherType type;

	public FileExistsMatcher(File file, FileExistsMatcherType type) {
		this.expected = file;
		this.type = type;
	}

	public boolean matches(Object actual) {
		if (this.type == FileExistsMatcherType.ISEXISTS) {
			return this.expected.exists();
		} else {
			return !this.expected.exists();
		}
	}

	public void describeTo(Description description) {
		description.appendText(String.format(type.description(), this.expected.getAbsolutePath()));
	}

	public static enum FileExistsMatcherType {
		ISEXISTS {
			@Override
			public String description() {
				return "file '%s' has existed";
			}
		},
		UNEXISTS {
			@Override
			public String description() {
				return "file '%s' doesn't exist";
			}
		};
		public abstract String description();
	}
}
