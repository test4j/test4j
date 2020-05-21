package org.test4j.module.database.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ScriptTable：数据库表名
 * 和 FluentMybatis库中的TableName作用一样，这里单独定义，方便test4j可以单独使用
 *
 * @author:darui.wu Created by darui.wu on 2020/5/7.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ScriptTable {
    /**
     * 数据库表名
     *
     * @return
     */
    String value();
}