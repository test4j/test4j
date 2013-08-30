package org.test4j.spec.txt;

import java.util.Map;

import org.test4j.junit.JSpec;
import org.test4j.spec.annotations.Given;
import org.test4j.spec.annotations.Named;
import org.test4j.spec.annotations.StoryFile;
import org.test4j.spec.annotations.StorySource;
import org.test4j.spec.annotations.StoryType;
import org.test4j.spec.annotations.Then;

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
