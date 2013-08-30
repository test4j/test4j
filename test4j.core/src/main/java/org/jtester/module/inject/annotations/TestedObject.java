package org.jtester.module.inject.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 用来表明被测对象，方便Inject属性
 * 
 * @author darui.wudr 2013-1-9 下午1:50:38
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface TestedObject {
}
