package org.jtester.spec.txt;

import java.util.Map;

import org.jtester.junit.JSpec;
import org.jtester.spec.annotations.Given;
import org.jtester.spec.annotations.Named;
import org.jtester.spec.annotations.StoryFile;
import org.jtester.spec.annotations.StorySource;
import org.jtester.spec.annotations.StoryType;
import org.jtester.spec.annotations.Then;

@StoryFile(type = StoryType.TXT, source = StorySource.ClassPath)
public class MapConvertDemo extends JSpec {
    private Map<String, String> map;

    @Given
    public void giveAMapPara(final @Named("map") Map<String, String> map// <br>
    ) throws Exception {
        this.map = map;
    }

    @Then
    public void checkMap(final @Named("map") DataMap map// <br>
    ) throws Exception {
        want.map(this.map).propertyEqMap(map);
    }
}
