package org.test4j.spec.scenario;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.test4j.spec.annotations.StoryType;
import org.test4j.spec.exceptions.SkipScenarioException;
import org.test4j.spec.inner.IScenario;
import org.test4j.spec.inner.IScenarioStep;

public abstract class JSpecScenario implements IScenario {
    protected int                       index;

    protected String                    scenario;

    protected String                    description;

    protected final List<IScenarioStep> steps  = new ArrayList<IScenarioStep>();

    /**
     * 本测试场景是否设置跳过
     */
    protected boolean                   isSkip = false;

    /**
     * 是否是跳过场景
     * 
     * @return
     */
    public final boolean isSkip() {
        return this.isSkip;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    public String getScenario() {
        return scenario;
    }

    /**
     * 返回场景的步骤
     * 
     * @return
     */
    public final List<IScenarioStep> getSteps() {
        return steps;
    }

    public final String getName() {
        return String.format("%02d - %s", index, scenario);
    }

    public final String getDescription() {
        return description;
    }

    /**
     * 验证场景合法性
     */
    public void validate() throws Throwable {
        if (this.isSkip()) {
            throw new SkipScenarioException(this);
        }
    }

    public static List<IScenario> parseFrom(StoryType type, String story) {
        if (type == StoryType.XML) {
            return XmlJSpecScenario.parseJSpecScenarioFrom(story, "utf-8");
        } else {
            return TxtJSpecScenario.parseJSpecScenarioFrom(story);
        }
    }

    public static List<IScenario> parseFrom(StoryType type, InputStream stream, String encoding) {
        if (type == StoryType.XML) {
            return XmlJSpecScenario.parseJSpecScenarioFrom(stream, encoding);
        } else {
            return TxtJSpecScenario.parseJSpecScenarioFrom(stream, encoding);
        }
    }

    @Override
    public final String toString() {
        return this.scenario.replaceAll("\\s", " ");
    }
}
