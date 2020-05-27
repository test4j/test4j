package org.test4j.generator.properties;

import org.test4j.tools.commons.TextBuilder;

import java.util.*;

/**
 * ConfigGenerator
 *
 * @author darui.wu
 * @create 2020/4/23 9:40 下午
 */
public class ConfigGenerator {

    private final static List<String> filter_keys = Arrays.asList("");

    public final Map<String, String> properties = new TreeMap<>();

    /**
     * 生成对应环境的配置
     *
     * @param env
     * @return
     * @throws Exception
     */
    public static ConfigGenerator env(String env) throws Exception {
        return new ConfigGenerator("application.yml", "application-" + env + ".yml");
    }

    public ConfigGenerator(String... files) throws Exception {
        this(ConfigGeneratorHelper.parseFiles(files));
    }

    public ConfigGenerator(Properties properties) {
        for (Enumeration<String> it = (Enumeration) properties.propertyNames(); it.hasMoreElements(); ) {
            String key = it.nextElement();
            this.properties.put(key, properties.getProperty(key));
        }
    }

    public ConfigGenerator(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            this.properties.put(entry.getKey(), entry.getValue());
        }
    }

    public String generate(Class klass) {
        return this.generate(klass.getPackage().getName(), klass.getSimpleName());
    }

    public String generate(String packageName, String klassName) {

        JavaGenerator generator = new JavaGenerator(klassName);
        for (Map.Entry<String, String> entry : this.properties.entrySet()) {
            String key = entry.getKey();
            if (filter_keys.contains(key)) {
                continue;
            }
            String[] items = key.split("\\.");
            generator.addKeys(items, key, entry.getValue());
        }
        TextBuilder appender = new TextBuilder().newLine()
            .append("package %s;", packageName).newLine(2)
            .append("import org.springframework.core.env.Environment;").newLine()
            .append("import org.springframework.stereotype.Component;").newLine()
            .append("@Component")
            .append(generator.toJavaStr(true, ""));
        return appender.toString();
    }
}