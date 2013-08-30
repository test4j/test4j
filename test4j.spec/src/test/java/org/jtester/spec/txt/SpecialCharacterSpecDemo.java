package org.jtester.spec.txt;

import org.jtester.spec.annotations.StoryFile;
import org.jtester.spec.annotations.StorySource;
import org.jtester.spec.annotations.StoryType;
import org.test4j.junit.JSpec;
import org.test4j.spec.annotations.Named;
import org.test4j.spec.annotations.Then;

@StoryFile(type = StoryType.TXT, source = StorySource.ClassPath)
public class SpecialCharacterSpecDemo extends JSpec {
    @Then
    public void checkString(final @Named("字符串") String input// <br>
    ) throws Exception {
        want.string(input).notNull();
    }
}
