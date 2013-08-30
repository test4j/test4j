package org.jtester.module.dbfit.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

/**
 * wiki参数
 * 
 * @author darui.wudr
 * 
 */
@Retention(RUNTIME)
public @interface FitVar {
	String key();

	String value();
}
