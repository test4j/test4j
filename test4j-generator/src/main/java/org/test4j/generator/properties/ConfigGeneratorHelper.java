package org.test4j.generator.properties;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * ConfigGeneratorHelper
 *
 * @author darui.wu
 * @create 2020/4/24 6:00 下午
 */
final class ConfigGeneratorHelper {
    final static Map<String, String> parseFiles(String[] files) throws Exception {
        Map<String, String> all = new HashMap<>();
        for (String file : files) {
            try (InputStream input = ConfigGeneratorHelper.class.getClassLoader().getResourceAsStream(file)) {
                if (file.endsWith(".yml")) {
                    Map<String, Object> map = new Yaml().load(input);
                    all.putAll(yaml("", map));
                } else if (file.endsWith(".properties")) {

                } else {
                    throw new RuntimeException("not support file");
                }
            }
        }
        return all;
    }

    final static Map<String, String> yaml(String prefix, Map<String, Object> map) {
        Map<String, String> all = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String || value instanceof Integer) {
                all.put(prefix + entry.getKey(), String.valueOf(value));
            } else if (value instanceof Map) {
                all.putAll(yaml(prefix + entry.getKey() + ".", (Map<String, Object>) value));
            } else {
                throw new RuntimeException("not support type: " + value.getClass().getName());
            }
        }
        return all;
    }
}