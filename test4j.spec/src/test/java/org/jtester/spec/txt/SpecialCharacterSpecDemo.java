package org.jtester.spec.txt;

import org.jtester.junit.JSpec;
import org.jtester.spec.annotations.Named;
import org.jtester.spec.annotations.StoryFile;
import org.jtester.spec.annotations.StorySource;
import org.jtester.spec.annotations.StoryType;
import org.jtester.spec.annotations.Then;

@StoryFile(type = StoryType.TXT, source = StorySource.ClassPath)
public class SpecialCharacterSpecDemo extends JSpec {
    @Then
    public void checkString(final @Named("字符串") String input// <br>
    ) throws Exception {
        want.string(input).notNull();
    }
}
