package org.jtester.junit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The <code>ClasspathProperty</code> specifies the System property name used to
 * retrieve the java classpath which is searched for Test classes and suites.
 * Default is "java.class.path".
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ClasspathProperty {
	public String value();
}
