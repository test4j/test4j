package org.jtester.spec.txt;

import org.jtester.junit.JSpec;
import org.jtester.spec.annotations.Given;
import org.jtester.spec.annotations.Named;
import org.jtester.spec.annotations.StoryFile;
import org.jtester.spec.annotations.StoryType;
import org.jtester.spec.annotations.Then;

@StoryFile(type = StoryType.TXT)
public class TemplateMethodSpec extends JSpec {

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
