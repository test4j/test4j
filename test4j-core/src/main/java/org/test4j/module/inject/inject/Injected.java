package org.test4j.module.inject.inject;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 用来表明被测对象，方便Inject属性
 * 
 * @author darui.wudr 2013-1-9 下午1:50:38
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Injected {
}