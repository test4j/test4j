package org.jtester.module.tracer.annotations;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.jtester.tools.commons.JSONHelper;
import org.jtester.tools.commons.StringHelper;

@Deprecated
@Retention(RUNTIME)
@Target( { METHOD, TYPE, CONSTRUCTOR })
public @interface Tracer {
	/**
	 * 跟踪spring调用信息
	 * 
	 * @return
	 */
	boolean spring() default true;

	/**
	 * 跟踪jdbc的sql信息
	 * 
	 * @return
	 */
	boolean jdbc() default true;

	/**
	 * 默认是把参数和返回值信息转换为json字符串形式<br>
	 * 当个别参数内容特别多或转换json出错时，建议改为toString()形式
	 * 
	 * @return
	 */
	Info info() default Info.TOJSON;

	public static enum Info {
		TOSTRING {
			@Override
			public String toInfoString(Object o) {
				return o == null ? "<null>" : o.toString();
			}
		},
		TOJSON {
			@Override
			public String toInfoString(Object o) {
				if (o == null) {
					return "<null>";
				}
				try {
					return JSONHelper.toJSON(o);
				} catch (Throwable e) {
					return "to json error:" + StringHelper.exceptionTrace(e);
				}
			}
		};

		public abstract String toInfoString(Object o);
	}
}
