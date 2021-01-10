package org.test4j.module.spec.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JSpec步骤定义片段文件混合
 *
 * @author darui.wudr 2013-6-3 下午7:02:39
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Mix {
}