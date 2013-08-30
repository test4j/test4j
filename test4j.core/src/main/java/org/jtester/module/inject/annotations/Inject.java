package org.jtester.module.inject.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(FIELD)
@Retention(RUNTIME)
public @interface Inject {
	/**
	 * mock对象要注入的目标，以当前testObject的字段名称表示<br>
	 * 支持ongl表达
	 * 
	 * @return
	 */
	String[] targets() default {};

	/**
	 * mock对象要注入到目标对象的那个变量中<br>
	 * 如果值为空或null，表示按类型方式注入
	 * 
	 * @return
	 */
	String[] properties() default {};
}
