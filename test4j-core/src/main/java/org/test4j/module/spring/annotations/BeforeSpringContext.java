package org.test4j.module.spring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 用于指定spring bean的初始化方法<br>
 * 一个类中只能有一个方法带有这个属性，如果有多个则取第一个方法
 * 
 * @author darui.wudr
 * 
 */
@Target(ElementType.METHOD)
@Retention(RUNTIME)
public @interface BeforeSpringContext {

}
