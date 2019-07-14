package org.test4j.hamcrest.iassert.impl;

import java.io.File;


import org.hamcrest.Matcher;
import org.test4j.hamcrest.iassert.interal.Assert;
import org.test4j.hamcrest.iassert.intf.IFileAssert;
import org.test4j.hamcrest.matcher.file.FileExistsMatcher;
import org.test4j.hamcrest.matcher.file.FileMatchers;
import org.test4j.hamcrest.matcher.file.FileExistsMatcher.FileExistsMatcherType;

public class FileAssert extends Assert<File, IFileAssert> implements IFileAssert {
    public FileAssert() {
        super(File.class, IFileAssert.class);
    }

    public FileAssert(File file) {
        super(file, File.class, IFileAssert.class);
    }

    public IFileAssert isExists() {
        FileExistsMatcher matcher = new FileExistsMatcher((File) this.getAssertObject().getValue(),
                FileExistsMatcherType.ISEXISTS);
        return this.assertThat(matcher);
    }

    public IFileAssert unExists() {
        FileExistsMatcher matcher = new FileExistsMatcher((File) this.getAssertObject().getValue(),
                FileExistsMatcherType.UNEXISTS);
        return this.assertThat(matcher);
    }

    public IFileAssert nameContain(String expected) {
        Matcher<?> matcher = FileMatchers.nameContain(expected);
        return this.assertThat(matcher);
    }

    public IFileAssert nameEq(String expected) {
        Matcher<?> matcher = FileMatchers.nameEq(expected);
        return this.assertThat(matcher);
    }
}
