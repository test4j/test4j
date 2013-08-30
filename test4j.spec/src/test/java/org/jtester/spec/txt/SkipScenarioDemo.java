package org.jtester.spec.txt;

import org.jtester.junit.JSpec;
import org.jtester.spec.annotations.StoryFile;
import org.jtester.spec.annotations.StorySource;
import org.jtester.spec.annotations.StoryType;
import org.jtester.spec.annotations.When;
import org.jtester.spec.inner.IScenario;

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
