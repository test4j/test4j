package org.test4j.generator.engine;

import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.test4j.tools.commons.StringHelper;

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
    public VelocityTemplateEngine init() {
        if (null == velocityEngine) {
            Properties p = new Properties();
            p.setProperty(VM_LOAD_PATH_KEY, VM_LOAD_PATH_VALUE);
            p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, "");
            p.setProperty(Velocity.ENCODING_DEFAULT, UTF8);
            p.setProperty(Velocity.INPUT_ENCODING, UTF8);
            p.setProperty("file.resource.loader.unicode", "true");
            velocityEngine = new VelocityEngine(p);
        }
        return this;
    }


    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        if (StringHelper.isBlank(templatePath)) {
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