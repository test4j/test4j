package org.test4j.generator;

import org.test4j.generator.config.IGlobalConfig;
import org.test4j.generator.impl.GeneratorByAnnotation;
import org.test4j.generator.impl.GeneratorByApi;

/**
 * Entity文件生成器
 *
 * @author wudarui
 */
public class FileGenerator {
    /**
     * 根据class上的主键生成文件
     * <p>
     * 根据@Tables上是否设置了 srcDir 来判断是否生成配套的test辅助类
     * 根据@Tables上是否设置了 testDir 来判断是否生成配套的test辅助类
     *
     * @param classes
     */
    public static void build(Class... classes) {
        for (Class clazz : classes) {
            GeneratorByAnnotation.generate(clazz);
        }
    }

    /**
     * 通过java编码生成文件
     *
     * @param withEntity true: 生成Entity
     * @param withTest   true: 生成test辅助类
     * @return
     */
    public static IGlobalConfig build(boolean withEntity, boolean withTest) {
        return GeneratorByApi.build(withEntity, withTest);
    }
}