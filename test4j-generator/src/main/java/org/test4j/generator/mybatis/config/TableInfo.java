package org.test4j.generator.mybatis.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.test4j.generator.mybatis.config.constant.DefinedColumn;
import org.test4j.generator.mybatis.config.constant.Naming;
import org.test4j.generator.mybatis.config.constant.OutputDir;
import org.test4j.generator.mybatis.db.DbType;
import org.test4j.generator.mybatis.db.IDbQuery;
import org.test4j.generator.mybatis.db.IFieldCategory;
import org.test4j.generator.mybatis.db.IJavaType;
import org.test4j.generator.mybatis.db.query.H2Query;
import org.test4j.tools.commons.StringHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static org.test4j.generator.mybatis.config.constant.ConfigKey.*;

/**
 * 表信息，关联到当前字段信息
 *
 * @author darui.wu
 */
@Getter
@Accessors(chain = true)
public class TableInfo {
    /***********************************************/
    /****************以下是配置信息******************/
    /***********************************************/
    /**
     * 表名
     */
    @Setter
    private String tableName;
    /**
     * entity前缀部分
     */
    @Setter
    private String entityPrefix;

    /**
     * 是否作分库分表处理
     */
    @Setter
    private boolean isPartition = false;
    /**
     * 主键的sequence name指定
     */
    @Setter
    private String seqName;
    /**
     * 特殊字段指定(排除，别名等)
     */
    private Map<String, DefinedColumn> columns = new HashMap<String, DefinedColumn>();

    /***********************************************/
    /****************以下是数据库信息*****************/
    /***********************************************/
    /**
     * 注释
     */
    @Setter
    private String comment;
    /**
     * 字段列表
     */
    private List<TableField> fields = new ArrayList<>();
    /**
     * 所有字段拼接串
     */
    private String fieldNames;

    /***********************************************/
    /*************以下外部导入或内部初始化*************/
    /***********************************************/
    /**
     * 全局配置
     */
    private final GlobalConfig globalConfig;
    /**
     * 表配置
     */
    private final TableConfig tableConfig;
    /**
     * 所有字段类型列表
     */
    private final Set<String> importTypes = new HashSet<>();
    /**
     * 执行模板生成各个步骤产生的上下文信息，比如Mapper名称等，供其他模板生成时引用
     */
    private Map<IFieldCategory, String> fileTypeName = new HashMap<>();

    public TableInfo(String tableName, GlobalConfig globalConfig, TableConfig tableConfig) {
        this(tableName, null, globalConfig, tableConfig);
    }


    public TableInfo(String tableName, String entityPrefix, GlobalConfig globalConfig, TableConfig tableConfig) {
        this.tableName = tableName;
        this.entityPrefix = entityPrefix;
        this.globalConfig = globalConfig;
        this.tableConfig = tableConfig;
    }

    /**
     * 指定特殊字段
     *
     * @param gmtCreate    记录创建时间
     * @param gmtModified  记录修改时间
     * @param logicDeleted 记录逻辑删除字段
     * @return
     */
    public TableInfo setColumn(String gmtCreate, String gmtModified, String logicDeleted) {
        this.setGmtCreate(gmtCreate);
        this.setGmtModified(gmtModified);
        this.setLogicDeleted(logicDeleted);
        return this;
    }

    /**
     * 记录创建字段名称
     */
    @Getter(AccessLevel.NONE)
    private String gmtCreate;
    /**
     * 记录修改字段名称
     */
    @Getter(AccessLevel.NONE)
    private String gmtModified;
    /**
     * 逻辑删除字段名称
     */
    @Getter(AccessLevel.NONE)
    private String logicDeleted;

    public TableInfo setGmtCreate(String gmtCreate) {
        if (StringHelper.isBlank(this.gmtCreate)) {
            this.gmtCreate = gmtCreate;
        }
        return this;
    }

    public TableInfo setGmtModified(String gmtModified) {
        if (StringHelper.isBlank(this.gmtModified)) {
            this.gmtModified = gmtModified;
        }
        return this;
    }

    public TableInfo setLogicDeleted(String logicDeleted) {
        if (StringHelper.isBlank(this.logicDeleted)) {
            this.logicDeleted = logicDeleted;
        }
        return this;
    }

    public TableInfo column(String columnName, IJavaType columnType) {
        return this.column(columnName, null, columnType);
    }

    public TableInfo column(String columnName, String propertyName) {
        return this.column(columnName, propertyName, null);
    }

    public TableInfo column(String columnName, String propertyName, IJavaType columnType) {
        this.columns.put(columnName, new DefinedColumn(columnName, propertyName, columnType));
        return this;
    }

    public TableInfo exclude(String... columnNames) {
        for (String column : columnNames) {
            this.columns.put(column, new DefinedColumn(column).setExclude(true));
        }
        return this;
    }

    /**
     * 字段是否被排除
     *
     * @param field
     * @return
     */
    private boolean isExclude(String field) {
        DefinedColumn column = this.columns.get(field);
        return column != null && column.isExclude();
    }

    /**
     * 处理表  名称
     *
     * @return 根据策略返回处理后的名称
     */
    public void initTable() {
        this.initEntityPrefix();
        this.initTableFields();
    }

    private void initEntityPrefix() {
        if (!StringHelper.isBlank(this.entityPrefix)) {
            return;
        }
        String prefix = this.getNoPrefixTableName();
        if (globalConfig.getTableNaming() == Naming.underline_to_camel) {
            prefix = Naming.underlineToCamel(prefix);
        }
        this.entityPrefix = Naming.capitalFirst(prefix);
    }

    /**
     * 返回
     *
     * @return
     */
    public String getNoPrefixTableName() {
        if (this.tableConfig.needRemovePrefix()) {
            return Naming.removePrefix(this.tableName, this.tableConfig.getTablePrefix());
        } else {
            return this.tableName;
        }
    }

    /**
     * 是否已找到id字段
     */
    @Getter(AccessLevel.NONE)
    private transient boolean haveId = false;

    /**
     * 增加数据库字段
     *
     * @return 所有未排除字段
     */
    private List<TableField> initTableFields() {
        DbConfig dbConfig = this.globalConfig.getDbConfig();
        DbType dbType = dbConfig.getDbType();
        IDbQuery dbQuery = dbConfig.getDbQuery();

        this.fields = new ArrayList<>();
        String tableFieldsSql = this.buildTableFieldsSql();
        try (PreparedStatement preparedStatement = dbConfig.getConn().prepareStatement(tableFieldsSql); ResultSet results = preparedStatement.executeQuery()) {
            Set<String> h2PkColumns = this.h2PkColumns();
            while (results.next()) {
                TableField field = this.initTableField(dbType, dbQuery, h2PkColumns, results);
                String fieldName = Optional.ofNullable(field).map(TableField::getColumnName).orElse(null);
                if (field == null || this.isExclude(fieldName)) {
                    continue;
                }
                if (!field.isPrimary()) {
                    field.setCategory(this.getFieldCategory(fieldName));
                }
                this.fields.add(field);
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception：" + e.getMessage(), e);
        }
        Collections.sort(this.fields);
        this.fieldNames = fields.stream().map(TableField::getColumnName).collect(Collectors.joining(", "));
        return fields;
    }

    private IFieldCategory getFieldCategory(String fieldName) {
        if (fieldName.equalsIgnoreCase(this.gmtCreate)) {
            return IFieldCategory.GmtCreate;
        } else if (fieldName.equalsIgnoreCase(this.gmtModified)) {
            return IFieldCategory.GmtModified;
        } else if (fieldName.equalsIgnoreCase(this.logicDeleted)) {
            return IFieldCategory.IsDeleted;
        } else {
            return IFieldCategory.Common;
        }
    }

    private TableField initTableField(DbType dbType, IDbQuery dbQuery, Set<String> h2PkColumns, ResultSet results) throws SQLException {
        String columnName = results.getString(dbQuery.fieldName());
        if (this.isExclude(columnName)) {
            return null;
        }
        TableField field = new TableField(this, columnName);
        DefinedColumn defined = this.columns.get(columnName);
        if (defined != null && defined.getFieldName() != null) {
            field.setName(defined.getFieldName());
        }
        if (defined != null && defined.getJavaType() != null) {
            field.setJavaType(defined.getJavaType());
        }
        boolean primary = this.isPrimary(columnName, results, h2PkColumns);
        // 处理ID, 只取第一个，并放到list中的索引为0的位置
        if (primary && !haveId) {
            if (DbType.H2 == dbType || DbType.SQLITE == dbType || dbQuery.isKeyIdentity(results)) {
                field.setCategory(IFieldCategory.PrimaryId);
            } else {
                field.setCategory(IFieldCategory.PrimaryKey);
            }
            haveId = true;
        }
        field.setColumnType(results.getString(dbQuery.fieldType()));
        field.initNaming(results);
        return field;
    }

    /**
     * 是否主键字段
     *
     * @param columnName  字段名
     * @param results
     * @param h2PkColumns
     * @return 是否主键
     * @throws SQLException
     */
    private boolean isPrimary(String columnName, ResultSet results, Set<String> h2PkColumns) throws SQLException {
        DbConfig dbConfig = this.globalConfig.getDbConfig();
        DbType dbType = dbConfig.getDbType();

        if (DbType.H2 == dbType) {
            return h2PkColumns.contains(columnName);
        }
        IDbQuery dbQuery = dbConfig.getDbQuery();
        String key = results.getString(dbQuery.fieldKey());
        if (DbType.DB2 == dbType || DbType.SQLITE == dbType) {
            return !StringHelper.isBlank(key) && "1".equals(key);
        } else {
            return !StringHelper.isBlank(key) && "PRI".equals(key.toUpperCase());
        }
    }

    /**
     * 构造查询表字段信息语句
     *
     * @return 查询表字段信息语句
     */
    private String buildTableFieldsSql() {
        DbConfig dbConfig = this.globalConfig.getDbConfig();
        DbType dbType = dbConfig.getDbType();
        IDbQuery dbQuery = dbConfig.getDbQuery();
        String tableFieldsSql = dbQuery.tableFieldsSql();

        switch (dbType) {
            case POSTGRE_SQL:
            case DB2:
                return String.format(tableFieldsSql, dbConfig.getSchemaName(), tableName);
            case ORACLE:
                return String.format(tableFieldsSql.replace("#schema", dbConfig.getSchemaName()), tableName);
            default:
                return String.format(tableFieldsSql, tableName);
        }
    }

    /**
     * h2数据库需特殊处理
     *
     * @return
     * @throws SQLException
     */
    private Set<String> h2PkColumns() throws SQLException {
        DbConfig dbConfig = this.globalConfig.getDbConfig();
        Set<String> h2PkColumns = new HashSet<>();
        if (dbConfig.getDbType() != DbType.H2) {
            return h2PkColumns;
        }
        try (PreparedStatement pkQueryStmt = dbConfig.getConn().prepareStatement(String.format(H2Query.PK_QUERY_SQL, tableName));
             ResultSet pkResults = pkQueryStmt.executeQuery()) {
            IDbQuery dbQuery = dbConfig.getDbQuery();
            while (pkResults.next()) {
                String primaryKey = pkResults.getString(dbQuery.fieldKey());
                if (Boolean.valueOf(primaryKey)) {
                    h2PkColumns.add(pkResults.getString(dbQuery.fieldName()));
                }
            }
        }
        return h2PkColumns;
    }

    /**
     * base dao 导入的自定义接口
     * key: implements 接口完整定义，包含泛型
     * value: 接口import完整路径
     */
    @Setter
    @Getter
    private Map<String, String> baseDaoInterfaces = new HashMap<>();

    public TableInfo addBaseDaoInterface(String interfaceName, String interfacePackage) {
        this.baseDaoInterfaces.put(interfaceName, interfacePackage);
        return this;
    }

    /**
     * mapper类bean名称前缀
     */
    @Setter
    private String mapperBeanPrefix = "";

    public TableInfo setMapperPrefix(String mapperBeanPrefix) {
        this.mapperBeanPrefix = mapperBeanPrefix;
        return this;
    }

    public String outputDir(OutputDir dirType) {
        switch (dirType) {
            case Dao:
                return this.globalConfig.getDaoOutputDir() + this.globalConfig.getPackageDir();
            case Test:
                return this.globalConfig.getTestOutputDir() + this.globalConfig.getPackageDir();
            case Base:
            default:
                return this.globalConfig.getOutputDir() + this.globalConfig.getPackageDir();
        }
    }

    public String getBasePackage() {
        return globalConfig.getBasePackage();
    }

    private Map<String, Object> context;

    /**
     * 初始化模板的上下文变量
     *
     * @return
     */
    public Map<String, Object> initTemplateContext() {
        this.context = new HashMap<>();
        {
            context.put(KEY_TABLE, this.getTableName());
            context.put(KEY_TABLE_NO_PREFIX, this.getNoPrefixTableName());
            context.put(KEY_ENTITY_PREFIX, this.getEntityPrefix());
            for (TableField field : this.fields) {
                if (!field.isPrimary()) {
                    continue;
                }
                context.put(KEY_PRIMARY_COLUMN_NAME, field.getColumnName());
                context.put(KEY_PRIMARY_FIELD_NAME, field.getName());
                break;
            }
            context.put(KEY_COMMENT, this.getComment());
            context.put(KEY_FIELD_NAMES, this.getFieldNames());
            context.put(KEY_FIELDS, this.getFields());
            context.put(KEY_AUTHOR, this.globalConfig.getAuthor());
        }
        {
            String types = this.fields.stream()
                .map(field -> field.getJavaType().getImportName())
                .filter(type -> type != null)
                .distinct()
                .sorted()
                .map(type -> "import " + type + ";")
                .collect(Collectors.joining("\n"));
            context.put("importTypes", types);
        }
        return context;
    }
}