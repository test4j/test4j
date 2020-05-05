package org.test4j.generator.engine;

import lombok.extern.slf4j.Slf4j;
import org.test4j.generator.mybatis.config.BuildConfig;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * 模板引擎抽象类
 *
 * @author darui.wu
 */
@Slf4j
public abstract class AbstractTemplateEngine {
    public final static String UTF8 = StandardCharsets.UTF_8.name();
    public final static String VM_LOAD_PATH_KEY = "file.resource.loader.class";
    public final static String VM_LOAD_PATH_VALUE = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";


    /**
     * 配置信息
     */
    protected BuildConfig config;

    /**
     * 模板引擎初始化
     */
    public AbstractTemplateEngine init(BuildConfig config) {
        this.config = config;
        return this;
    }

    /**
     * 输出模板
     *
     * @param template
     */
    public void output(String template, Map<String, Object> config, String filePath) {
        try {
            File dir = new File(filePath).getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            this.writer(config, template, filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将模板转化成为文件
     *
     * @param objectMap    渲染对象 MAP 信息
     * @param templatePath 模板文件
     * @param outputFile   文件生成的目录
     */
    public abstract void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception;
}
