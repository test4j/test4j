package org.test4j.spec.xml;

import org.test4j.junit.JSpec;
import org.test4j.spec.annotations.Named;
import org.test4j.spec.annotations.StoryFile;
import org.test4j.spec.annotations.StorySource;
import org.test4j.spec.annotations.StoryType;
import org.test4j.spec.annotations.Then;

@StoryFile(value = "SpecialCharacterXMLSpecDemo.xml", type = StoryType.XML, source = StorySource.ClassPath)
public class SpecialCharacterXMLSpecDemo extends JSpec {
    @Then
    public void checkString(final @Named("字符串") String input// <br>
    ) throws Exception {
        want.string(input).notNull();
    }
}
