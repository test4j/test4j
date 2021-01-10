package org.test4j.module.spec;

import org.test4j.ISpring;
import org.test4j.asserts.IWant;
import org.test4j.module.spec.internal.IScenario;
import org.test4j.module.spec.internal.Scenario;
import org.test4j.tools.IUtils;

import java.io.Serializable;

public interface IStory extends Serializable, ISpring, IWant, IUtils {
    IScenario story = new Scenario();
}