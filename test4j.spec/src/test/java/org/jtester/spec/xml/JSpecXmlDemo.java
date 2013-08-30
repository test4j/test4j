package org.jtester.spec.xml;

import org.jtester.junit.JSpec;
import org.jtester.spec.annotations.Given;
import org.jtester.spec.annotations.Named;
import org.jtester.spec.annotations.StoryFile;
import org.jtester.spec.annotations.StorySource;
import org.jtester.spec.annotations.StoryType;
import org.jtester.spec.annotations.Then;
import org.jtester.spec.annotations.When;

@StoryFile(value = "JSpecXmlDemo.xml", type = StoryType.XML, source = StorySource.ClassPath)
public class JSpecXmlDemo extends JSpec {
    private String greeting    = "";

    private String guest       = "";

    private String housemaster = "";

    private String date        = "";

    @Given
    public void giveGreeting(@Named("guest") String guest,// <br>
                             @Named("greeting") String greeting,// <br>
                             @Named("housemaster") String housemaster,// <br>
                             @Named("date") String date// <br>
    ) {
        this.greeting = greeting;
        this.guest = guest;
        this.housemaster = housemaster;
        this.date = date;
    }

    String wholeword = "";

    @When
    public void doGreeting() {
        this.wholeword = String.format("%s %s %s on %s.", this.housemaster, this.greeting, this.guest, this.date);
    }

    @Then
    public void checkGreeting(@Named("完整句子") String expected) {
        want.string(this.wholeword).isEqualTo(expected);
    }
}
