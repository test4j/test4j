package org.test4j.generator.mybatis.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.test4j.generator.mybatis.config.DataSourceConfig;
import org.test4j.generator.mybatis.config.ITypeConvert;
import org.test4j.generator.mybatis.config.StrategyConfig;
import org.test4j.generator.mybatis.rule.DateType;
import org.test4j.generator.mybatis.rule.DbType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 全局构建配置项
 */
@Data
@Accessors(chain = true)
public class BuildConfig {
    private Generator generator;
    /**
     * 开发人员
     */
    private String author = "generate code";
    /**
     * 是否打开输出目录
     */
    private boolean open = true;
    /**
     * 是否覆盖已有文件
     */
    private boolean fileOverride = false;
    /**
     * 时间类型对应策略
     */
    private DateType dateType = DateType.TIME_PACK;
    /**
     * 开启 baseColumnList
     */
    private boolean baseColumnList = false;
    /**
     * 需要去掉的表前缀
     */
    @Setter(AccessLevel.NONE)
    private String[] tablePrefix;

    /**
     * 需要处理的表
     */
    @Getter
    private Map<String, TableInfo> tables = new HashMap<>();

    public BuildConfig() {
    }


    public TableInfo table(String tableName) {
        TableInfo table = new TableInfo(tableName, this);
        this.tables.put(tableName, table);
        return table;
    }

    public BuildConfig addTable(TableInfo table) {
        this.tables.put(table.getTableName(), table);
        return this;
    }

    public BuildConfig addTable(String tableName) {
        TableInfo table = new TableInfo(tableName, this);
        this.tables.put(tableName, table);
        return this;
    }

    public BuildConfig addTable(String tableName, boolean isPartition) {
        TableInfo table = new TableInfo(tableName, this)
            .setPartition(isPartition);
        this.tables.put(tableName, table);
        return this;
    }

    /**
     * 对所有表统一处理
     *
     * @param consumer
     * @return
     */
    public BuildConfig allTable(Consumer<TableInfo> consumer) {
        this.tables.values().stream().forEach(table -> consumer.accept(table));
        return this;
    }

    public BuildConfig setTablePrefix(String... tablePrefix) {
        this.tablePrefix = tablePrefix;
        return this;
    }

    /**
     * 表名称包含指定前缀
     *
     * @param tableName 表名称
     */
    public boolean containsTablePrefix(String tableName) {
        String[] tablePrefix = getTablePrefix();
        if (null == tablePrefix) {
            return false;
        }
        for (String prefix : tablePrefix) {
            if (tableName.contains(prefix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否需要做移除前缀处理
     *
     * @return
     */
    public boolean needRemovePrefix() {
        return this.tablePrefix != null && this.tablePrefix.length != 0;
    }

    private Map<FileType, String> fileNameFormat = new HashMap<FileType, String>() {
        {
            this.put(FileType.Entity, "%sEntity");
        }
    };
}
