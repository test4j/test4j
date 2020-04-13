package org.test4j.hamcrest.matcher.file;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.io.File;


public class FileExistsMatcher extends BaseMatcher<File> {
    private File expected;

    private FileExistsMatcherType type;

    public FileExistsMatcher(File file, FileExistsMatcherType type) {
        this.expected = file;
        this.type = type;
    }

    @Override
    public boolean matches(Object actual) {
        if (this.type == FileExistsMatcherType.ISEXISTS) {
            return this.expected.exists();
        } else {
            return !this.expected.exists();
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format(type.description(), this.expected.getAbsolutePath()));
    }

    public enum FileExistsMatcherType {
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
