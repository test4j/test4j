package org.test4j.generator.annotation;

import org.test4j.generator.db.DbType;

import java.lang.annotation.*;

import static org.test4j.generator.config.constant.ConfigKey.NOT_DEFINED;

/**
 * 需要Entity类表定义
 *
 * @author wudarui
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Tables {
    /**
     * 数据库类型
     *
     * @return
     */
    DbType dbType() default DbType.MYSQL;

    /**
     * 数据库链接url
     *
     * @return
     */
    String url();

    /**
     * 数据库用户名
     *
     * @return
     */
    String username();

    /**
     * 数据库用户密码
     *
     * @return
     */
    String password();

    /**
     * FluentMybatis Entity代码目录
     * 相对于跟目录System.getProperty("user.dir")路径
     * 一般是 "src/main/java"
     * 或者是 "subProject/src/main/java"
     *
     * @return
     */
    String srcDir() default "";

    /**
     * 辅助测试代码目录
     * 相对于跟目录System.getProperty("user.dir")路径
     * 一般是 "src/test/java"
     * 或者是 "subProject/src/test/java"
     *
     * @return
     */
    String testDir() default "";

    /**
     * dao接口和实现默认生成路径, 当srcDir有值时有效
     * 相对于跟目录System.getProperty("user.dir")路径
     * 默认生成在target目录下, 如果想生成到工程代码中
     * 一般是 "src/main/java"
     * 或者是 "subProject/src/main/java"
     *
     * @return
     */
    String daoDir() default "target/generate";

    /**
     * 生成文件的base package路径, 不包含 ".entity", ".dao"部分
     * 默认和生成定义类相同
     *
     * @return
     */
    String basePack() default "";

    /**
     * 指定数据库表名
     *
     * @return
     */
    Table[] tables();

    /**
     * ========== 下面定义可以被 @Table 定义覆盖 ==========
     */
    /**
     * 生成Entity文件时, 需要去除的表前缀
     *
     * @return
     */
    String[] tablePrefix() default {NOT_DEFINED};

    /**
     * 生成Mapper bean时在bean name前缀
     *
     * @return
     */
    String mapperPrefix() default NOT_DEFINED;

    /**
     * 记录创建字段
     *
     * @return
     */
    String gmtCreated() default NOT_DEFINED;

    /**
     * 记录修改字段
     *
     * @return
     */
    String gmtModified() default NOT_DEFINED;

    /**
     * 逻辑删除字段
     *
     * @return
     */
    String logicDeleted() default NOT_DEFINED;
}