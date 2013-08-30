package org.jtester.spec.xml;

import org.jtester.junit.JSpec;
import org.jtester.spec.annotations.Named;
import org.jtester.spec.annotations.StoryFile;
import org.jtester.spec.annotations.StorySource;
import org.jtester.spec.annotations.StoryType;
import org.jtester.spec.annotations.Then;

@StoryFile(value = "SpecialCharacterXMLSpecDemo.xml", type = StoryType.XML, source = StorySource.ClassPath)
public class SpecialCharacterXMLSpecDemo extends JSpec {
    @Then
    public void checkString(final @Named("字符串") String input// <br>
    ) throws Exception {
        want.string(input).notNull();
    }
}
