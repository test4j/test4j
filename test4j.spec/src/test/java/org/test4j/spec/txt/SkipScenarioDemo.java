package org.test4j.spec.txt;

import org.test4j.junit.JSpec;
import org.test4j.spec.annotations.StoryFile;
import org.test4j.spec.annotations.StorySource;
import org.test4j.spec.annotations.StoryType;
import org.test4j.spec.annotations.When;
import org.test4j.spec.inner.IScenario;

@StoryFile(type = StoryType.TXT, source = StorySource.ClassPath)
public class SkipScenarioDemo extends JSpec {
    int skip = 0;

    @Override
    public void runScenario(IScenario scenario) throws Exception {
        try {
            super.runScenario(scenario);
        } catch (Throwable e) {
            skip++;
        }
    }

    @When
    public void emptyMethod() throws Exception {
        want.number(skip).isEqualTo(1);
    }
}
