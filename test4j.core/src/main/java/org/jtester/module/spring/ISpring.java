package org.jtester.module.spring;

import org.jtester.module.core.ICoreInitial;
import org.jtester.module.spring.util.ISpringHelper;

public interface ISpring {
    final ISpringHelper spring = ICoreInitial.initSpringHelper();
}
