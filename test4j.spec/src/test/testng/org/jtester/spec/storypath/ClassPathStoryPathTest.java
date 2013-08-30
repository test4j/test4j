package org.jtester.spec.storypath;

import java.util.List;

import mockit.Mock;

import org.jtester.spec.annotations.StoryFile;
import org.jtester.spec.annotations.StoryType;
import org.jtester.spec.inner.IScenario;
import org.jtester.testng.JSpec;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

public class ClassPathStoryPathTest extends JTester {
    @Test(groups = "jspec")
    public void testGetStory() {
        StoryPath path = new ClassPathStoryPath(SpecDemo.class);
        new MockUp<ClassPathStoryPath>() {
            @Mock
            public String getStoryFile(StoryType type, StoryFile story) {
                return "org/jtester/spec/scenario/TxtJSpecScenarioTest.testParseSpecScenarioFrom.story";
            }
        };

        new MockUp<StoryPath>() {
            @Mock
            public StoryType getStoryType(StoryFile story) {
                return StoryType.TXT;
            }
        };
        want.object(path).propertyEq("path", "org/jtester/spec/storypath").propertyEq("name", "SpecDemo");
        List<IScenario> list = path.getStory(null, null);
        want.list(list).sizeEq(2);
    }

    @Test
    public void testGetStoryFile() {
        StoryFile story = SpecDemo.class.getAnnotation(StoryFile.class);
        new MockUp<StoryFile>() {
            @Mock
            public String value() {
                return "/test.story";
            }
        };
        String file = new ClassPathStoryPath(SpecDemo.class).getStoryFile(null, story);
        want.string(file).isEqualTo("test.story");
    }

    @Test
    public void testGetStoryFile_IsNull() {
        String file = new ClassPathStoryPath(SpecDemo.class).getStoryFile(null, null);
        want.string(file).isEqualTo(SpecDemo.class.getName().replace('.', '/') + ".story");
    }
}

@StoryFile(value = "classpath:test.story")
class SpecDemo extends JSpec {
    @Override
    public void runScenario(IScenario scenario) throws Throwable {
        this.run(scenario);
    }
}
