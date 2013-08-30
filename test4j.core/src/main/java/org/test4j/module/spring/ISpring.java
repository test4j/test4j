package org.test4j.module.spring;

import org.test4j.module.core.ICoreInitial;
import org.test4j.module.spring.util.ISpringHelper;

public interface ISpring {
    final ISpringHelper spring = ICoreInitial.initSpringHelper();
}
