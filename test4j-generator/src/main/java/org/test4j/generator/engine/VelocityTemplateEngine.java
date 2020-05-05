package org.test4j.generator.engine;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.test4j.generator.mybatis.config.BuildConfig;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Properties;

/**
 * Velocity 模板引擎实现文件输出
 *
 * @author darui.wu
 */
@Slf4j
public class VelocityTemplateEngine extends AbstractTemplateEngine {

    private VelocityEngine velocityEngine;

    @Override
    public VelocityTemplateEngine init(BuildConfig config) {
        super.init(config);
        if (null == velocityEngine) {
            Properties p = new Properties();
            p.setProperty(VM_LOAD_PATH_KEY, VM_LOAD_PATH_VALUE);
            p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, StringPool.EMPTY);
            p.setProperty(Velocity.ENCODING_DEFAULT, UTF8);
            p.setProperty(Velocity.INPUT_ENCODING, UTF8);
            p.setProperty("file.resource.loader.unicode", StringPool.TRUE);
            velocityEngine = new VelocityEngine(p);
        }
        return this;
    }


    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        if (StringUtils.isEmpty(templatePath)) {
            return;
        }
        Template template = velocityEngine.getTemplate(templatePath, UTF8);
        try (FileOutputStream fos = new FileOutputStream(outputFile);
             OutputStreamWriter ow = new OutputStreamWriter(fos, UTF8);
             BufferedWriter writer = new BufferedWriter(ow)) {
            template.merge(new VelocityContext(objectMap), writer);
        }
        log.debug("模板:" + templatePath + ";  文件:" + outputFile);
    }
}
