package org.test4j.module.spring;

import org.test4j.module.core.internal.ICoreInitial;
import org.test4j.module.spring.interal.ISpringHelper;

public interface ISpring {
    final ISpringHelper spring = ICoreInitial.initSpringHelper();
}
