package org.test4j.generator.mybatis.model;

import org.test4j.generator.engine.VelocityTemplateEngine;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成的对象
 *
 * @author darui.wu
 * @create 2019/10/17 1:39 下午
 */
@Data
@Accessors(chain = true)
public class GenerateObj {

    private String tableName;

    private String entityPrefix;

    private String camelEntity;

    private String tableMixName;

    private String mixInstance;

    private String mixCleanMethod;

    private String mappingName;

    private String tableMap;

    private String entityMap;


    public static GenerateObj init(TableInfo table, Map<String, Object> context) {
        GenerateObj obj = new GenerateObj()
            .setTableName(table.getTableName())
            .setEntityPrefix(table.getEntityPrefix())
            .setTableMixName(getConfig(context, "tableMix", "name"))
            .setMixCleanMethod(String.format("clean%sTable", table.getEntityPrefix()))
            .setMappingName(getConfig(context, "mapping", "name"))
            .setTableMap(getConfig(context, "tableMap", "name"))
            .setEntityMap(getConfig(context, "entityMap", "name"));
        obj.setCamelEntity(obj.entityPrefix.substring(0, 1).toLowerCase() + obj.entityPrefix.substring(1));
        obj.mixInstance = obj.tableMixName.substring(0, 1).toLowerCase() + obj.tableMixName.substring(1);
        if (obj.tableName.startsWith("t_")) {
            obj.tableName = obj.tableName.substring(2);
        }
        return obj;
    }

    private static String getConfig(Map<String, Object> context, String... keys) {
        Object item = context;
        String keyJoining = "";
        for (String key : keys) {
            if (item instanceof Map) {
                item = ((Map) item).get(key);
            } else {
                throw new RuntimeException("the property[" + keyJoining + "] is not a map.");
            }
            keyJoining = keyJoining + (keyJoining.isEmpty() ? "" : ".") + key;
        }
        if (item instanceof String) {
            return (String) item;
        } else {
            throw new RuntimeException("the property[" + keyJoining + "] is not a string.");
        }
    }

    public static void generate(List<GenerateObj> objs, String output, String testOutput, String basePackage) {
        VelocityTemplateEngine engine = new VelocityTemplateEngine().init(null);
        Map<String, Object> config = new HashMap<>();
        config.put("basePackage", basePackage);
        config.put("objs", objs);
        String templateDir = "/templates/";
        String srcPackDir = String.format("%s/%s/", output, basePackage.replace('.', '/'));
        String testPackDir = String.format("%s/%s/", testOutput, basePackage.replace('.', '/'));

        try {
            engine.writer(config, templateDir + "mix/Mixes.java.vm", testPackDir + "TableMixes.java");
            engine.writer(config, templateDir + "ITable.java.vm", testPackDir + "ITable.java");
            engine.writer(config, templateDir + "DataSourceScript.java.vm", testPackDir + "DataSourceScript.java");

            engine.writer(config, templateDir + "datamap/TM.java.vm", testPackDir + "datamap/TM.java");
            engine.writer(config, templateDir + "datamap/EM.java.vm", testPackDir + "datamap/EM.java");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}