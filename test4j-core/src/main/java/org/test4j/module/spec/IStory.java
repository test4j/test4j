package org.test4j.module.spec;

import org.test4j.ICore;
import org.test4j.ISpring;
import org.test4j.module.database.IDatabase;
import org.test4j.module.spec.internal.IScenario;
import org.test4j.module.spec.internal.Scenario;

import java.io.Serializable;

public interface IStory extends Serializable, ICore, IDatabase, ISpring {
    IScenario story = new Scenario();
}