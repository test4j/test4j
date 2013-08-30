package org.test4j.spec.xml;

import org.test4j.junit.JSpec;
import org.test4j.spec.annotations.Given;
import org.test4j.spec.annotations.Named;
import org.test4j.spec.annotations.StoryFile;
import org.test4j.spec.annotations.StoryType;
import org.test4j.spec.annotations.Then;

@StoryFile(type = StoryType.XML, value = "TemplateMethodXMLSpec.xml")
public class TemplateMethodXMLSpec extends JSpec {

    private Integer[] intArray;

    private DataMap   map;

    @Given
    public void initCondiction(final @Named("列表") Integer[] intArray,// <br>
                               final @Named("Map对象") DataMap map// <br>
    ) throws Exception {
        this.intArray = intArray;
        this.map = map;
    }

    @Then
    public void checkCondiction(final @Named("列表") Integer[] intArray, // <br>
                                final @Named("Map对象") DataMap map// <br>
    ) throws Exception {
        want.array(this.intArray).reflectionEq(intArray);
        want.map(this.map).reflectionEqMap(map);
    }
}
