package org.test4j.generator.mybatis.template;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.test4j.generator.mybatis.model.BuildConfig;
import org.test4j.generator.mybatis.model.ConfigKey;
import org.test4j.generator.mybatis.model.TableInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据表信息生成文件
 *
 * @author darui.wu
 */
@Data
@Accessors(chain = true)
public abstract class AbstractTableTemplate implements ConfigKey {
    /**
     * 模板内容
     */
    protected String template;
    /**
     * 生成的文件路径
     */
    protected String filePath;
    /**
     * 文件名称(不包含路径和后缀)
     */
    @Setter(AccessLevel.NONE)
    protected String fileName;

    protected void setFileName(String fileName) {
        int first = fileName.lastIndexOf('/');
        int last = fileName.lastIndexOf('.');
        this.fileName = fileName.substring(first == -1 ? 0 : first + 1, last == -1 ? fileName.length() - 1 : last);
    }

    protected String fileNameReg;

    private TemplateType templateType = TemplateType.Base;
    /**
     * 是否base dao
     */
    @Setter
    private boolean isBaseDao = false;
    /**
     * 是否分库
     */
    @Setter
    private boolean isPartition = false;

    private AbstractTableTemplate() {
    }

    public AbstractTableTemplate(String template, String fileNameReg) {
        this.fileNameReg = fileNameReg;
        this.template = template;
    }

    /**
     * 使用表信息初始化模板变量
     *
     * @param table
     * @return
     */
    public final Map<String, Object> initWith(TableInfo table) {
        this.filePath = table.getOutputDir() + "/" + this.fileNameReg.replace("*", table.getEntityPrefix());

        Map<String, Object> configs = new HashMap<>();
        {
            configs.put(KEY_TABLE, table.getTableName());
            configs.put(KEY_ENTITY_PREFIX, table.getEntityPrefix());
            configs.put(KEY_COMMENT, table.getComment());
            configs.put(KEY_FIELD_NAMES, table.getFieldNames());
            configs.put(KEY_FIELDS, table.getFields());
            configs.put(KEY_PRIMARY, table.getPrimary());
            configs.put(KEY_GMT_CREATE, table.getGmtCreateField());
            configs.put(KEY_GMT_MODIFIED, table.getGmtModifiedField());
            configs.put(KEY_IS_DELETED, table.getIsDeletedField());
        }
        this.templateConfigs(table, configs);
        return configs;
    }

    /**
     * 模板自身的配置项
     *
     * @param table
     * @param configs 原有的配置项
     * @return
     */
    protected abstract void templateConfigs(TableInfo table, Map<String, Object> configs);

    protected String getFileName(TableInfo table) {
        int start = this.fileNameReg.lastIndexOf('/');
        int end = this.fileNameReg.lastIndexOf('.');
        return this.fileNameReg.substring(start + 1, end).replace("*", table.getEntityPrefix());
    }

    protected String getPackage(TableInfo table) {
        int index = this.fileNameReg.lastIndexOf('/');
        String sub = "";
        if (index > 0) {
            sub = this.fileNameReg.substring(0, index).replace('/', '.');
        }
        return table.getBasePackage() + "." + sub;
    }

    /**
     * 模板类型
     */
    public enum TemplateType {
        Base,
        Test,
        Dao;
    }
}
