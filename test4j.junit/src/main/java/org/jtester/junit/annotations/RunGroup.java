package org.jtester.junit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface RunGroup {
	/**
	 * 要被运行的组
	 * 
	 * @return
	 */
	String[] includes() default {};

	/**
	 * 并排除运行的组
	 * 
	 * @return
	 */
	String[] excludes() default {};
}
