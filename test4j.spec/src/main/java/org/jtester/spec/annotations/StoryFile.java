package org.jtester.spec.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StoryFile {
    /**
     * 文件的的classpath路径,如果为空，则默认为当前package下同名文件。<br>
     * 此值对 source == Titian 无效
     * 
     * @return
     */
    String value() default "";

    /**
     * story内容格式
     * 
     * @return
     */
    StoryType type() default StoryType.DEFAULT;

    /**
     * 数据来源
     * 
     * @return
     */
    StorySource source() default StorySource.Default;

    /**
     * 文件编码
     * 
     * @return
     */
    String encoding() default "utf8";
}
