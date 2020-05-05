package org.test4j.generator.mybatis.model;

import lombok.Getter;
import lombok.Setter;
import org.test4j.generator.mybatis.config.*;
import org.test4j.generator.mybatis.db.IFieldCategory;
import org.test4j.generator.mybatis.db.IJavaType;
import org.test4j.generator.mybatis.db.query.H2Query;
import org.test4j.generator.mybatis.db.IDbQuery;
import org.test4j.generator.mybatis.db.DbType;
import lombok.experimental.Accessors;
import org.test4j.generator.mybatis.config.Naming;
import org.test4j.tools.commons.StringHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static org.test4j.generator.mybatis.config.ConfigKey.*;

/**
 * 表信息，关联到当前字段信息
 *
 * @author darui.wu
 */
@Getter
@Accessors(chain = true)
public class TableInfo {
    /**
     * 所有字段类型列表
     */
    private final Set<String> importTypes = new HashSet<>();
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
     * 记录创建字段名称
     */
    private String gmtCreate;
    /**
     * 记录修改字段名称
     */
    private String gmtModified;
    /**
     * 逻辑删除字段名称
     */
    private String logicDeleted;
    /**
     * 是否作分库分表处理
     */
    @Setter
    private boolean isPartition = false;

    private Map<String, TableColumn> columns = new HashMap<String, TableColumn>();
    /**
     * 执行模板生成各个步骤产生的上下文信息，比如Mapper名称等，供其他模板生成时引用
     */
    private Map<IFieldCategory, String> fileTypeName = new HashMap<>();

    /***********************************************/
    /****************以下是数据库信息******************/
    /***********************************************/
    /**
     * 注释
     */
    @Setter
    private String comment;
    /**
     * 主键字段
     */
    private TableField primary;
    /**
     * 记录创建字段
     */
    private TableField gmtCreateField;
    /**
     * 记录修改字段
     */
    private TableField gmtModifiedField;
    /**
     * 逻辑删除字段
     */
    private TableField isDeletedField;
    /**
     * 字段列表（除上面单列字段外）
     */
    private List<TableField> fields = new ArrayList<>();
    /**
     * 所有字段拼接串
     */
    private String fieldNames;

    public TableInfo(String tableName) {
        this.tableName = tableName;
    }

    public TableInfo(String tableName, String entityPrefix) {
        this.tableName = tableName;
        this.entityPrefix = entityPrefix;
    }

    public TableInfo(String tableName, BuildConfig buildConfig) {
        this.tableName = tableName;
        this.buildConfig = buildConfig;
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
        this.columns.put(columnName, new TableColumn(columnName, null, columnType));
        return this;
    }

    public TableInfo column(String columnName, String propertyName) {
        this.columns.put(columnName, new TableColumn(columnName, propertyName, null));
        return this;
    }

    public TableInfo exclude(String... columnNames) {
        for (String column : columnNames) {
            this.columns.put(column, new TableColumn(column).setExclude(true));
        }
        return this;
    }

    /**
     * 增加数据库字段
     *
     * @param field
     * @return true：添加成功， false：被排除字段
     */
    private boolean addField(TableField field) {
        String fieldName = field.getColumnName();
        if (this.isExclude(fieldName)) {
            return false;
        }
        if (this.gmtCreate.equalsIgnoreCase(fieldName)) {
            field.setCategory(IFieldCategory.GmtCreate);
            this.gmtCreateField = field;
        } else if (this.gmtModified.equalsIgnoreCase(fieldName)) {
            field.setCategory(IFieldCategory.GmtModified);
            this.gmtModifiedField = field;
        } else if (this.logicDeleted.equalsIgnoreCase(fieldName)) {
            field.setCategory(IFieldCategory.IsDeleted);
            this.isDeletedField = field;
        } else if (field.isPrimary()) {
            this.primary = field;
        } else {
            this.fields.add(field);
        }
        return true;
    }

    /**
     * 字段是否被排除
     *
     * @param field
     * @return
     */
    private boolean isExclude(String field) {
        TableColumn column = this.columns.get(field);
        return column != null && column.isExclude();
    }


    public TableInfo addImportTypes(String... types) {
        for (String type : types) {
            importTypes.add(type);
        }
        return this;
    }

    /**
     * 处理表  名称
     *
     * @return 根据策略返回处理后的名称
     */
    public void initTable() {
        if (StringHelper.isBlank(this.entityPrefix)) {
            this.entityPrefix = this.getEntityName();
        }
        this.initTableFields();
    }

    private String getEntityName() {
        StrategyConfig strategy = this.generator.getStrategyConfig();
        String propertyName = this.tableName;
        if (this.buildConfig.needRemovePrefix()) {
            propertyName = Naming.removePrefix(this.tableName, this.buildConfig.getTablePrefix());
        }
        if (strategy.getTableNaming() == Naming.underline_to_camel) {
            propertyName = Naming.underlineToCamel(this.tableName);
        }
        return Naming.capitalFirst(propertyName);
    }

    /**
     * 是否已找到id字段
     */
    private transient boolean haveId = false;

    /**
     * 将字段信息与表信息关联
     *
     * @return
     */
    private TableInfo initTableFields() {
        List<TableField> commons = this.findAllFields();
        this.fields = new ArrayList<>();
        if (this.primary != null) {
            this.fields.add(primary);
        }
        if (this.gmtCreateField != null) {
            this.fields.add(this.gmtCreateField);
        }
        if (this.gmtModified != null) {
            this.fields.add(this.gmtModifiedField);
        }
        if (this.isDeletedField != null) {
            this.fields.add(this.isDeletedField);
        }
        commons.sort(Comparator.comparing(TableField::getColumnName));
        this.fields.addAll(commons);
        this.fieldNames = fields.stream().map(TableField::getColumnName).collect(Collectors.joining(", "));

        return this;
    }

    private List<TableField> findAllFields() {
        DataSourceConfig dbConfig = this.generator.getDataSourceConfig();
        DbType dbType = dbConfig.getDbType();
        IDbQuery dbQuery = dbConfig.getDbQuery();

        this.fields = new ArrayList<>();
        try {
            Set<String> h2PkColumns = this.h2PkColumns();
            String tableFieldsSql = this.buildTableFieldsSql();
            try (PreparedStatement preparedStatement = dbConfig.getConn().prepareStatement(tableFieldsSql); ResultSet results = preparedStatement.executeQuery()) {
                while (results.next()) {
                    TableField field = this.initTableField(dbType, dbQuery, h2PkColumns, results);
                    if (field != null) {
                        this.addField(field);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception：" + e.getMessage(), e);
        }
        return fields;
    }

    private TableField initTableField(DbType dbType, IDbQuery dbQuery, Set<String> h2PkColumns, ResultSet results) throws SQLException {
        String columnName = results.getString(dbQuery.fieldName());
        if (this.isExclude(columnName)) {
            return null;
        }
        TableField field = new TableField(this, columnName);
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
     * @param columnName
     * @param results
     * @param h2PkColumns
     * @return
     * @throws SQLException
     */
    private boolean isPrimary(String columnName, ResultSet results, Set<String> h2PkColumns) throws SQLException {
        DataSourceConfig dbConfig = this.generator.getDataSourceConfig();
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
        DataSourceConfig dbConfig = this.generator.getDataSourceConfig();
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
        DataSourceConfig dbConfig = this.generator.getDataSourceConfig();
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

    private BuildConfig buildConfig;

    private Generator generator;

    public TableInfo setConfig(BuildConfig buildConfig, Generator generator) {
        this.buildConfig = buildConfig;
        this.generator = generator;
        return this;
    }

    String packDir;

    public String outputDir(OutputDir dirType) {
        if (packDir == null) {
            this.packDir = '/' + this.generator.getBasePackage().replace('.', '/') + "/";
        }
        switch (dirType) {
            case Dao:
                return this.getGenerator().getDaoOutputDir() + packDir;
            case Test:
                return this.getGenerator().getTestOutputDir() + packDir;
            case Base:
            default:
                return this.getGenerator().getOutputDir() + packDir;
        }
    }

    public String getBasePackage() {
        return generator.getBasePackage();
    }

    /**
     * 初始化模板的上下文变量
     *
     * @return
     */
    public Map<String, Object> initTemplateContext() {
        Map<String, Object> configs = new HashMap<>();
        {
            configs.put(KEY_TABLE, this.getTableName());
            configs.put(KEY_ENTITY_PREFIX, this.getEntityPrefix());
            configs.put(KEY_COMMENT, this.getComment());
            configs.put(KEY_FIELD_NAMES, this.getFieldNames());
            configs.put(KEY_FIELDS, this.getFields());
        }
        {
            String types = this.fields.stream()
                .map(field -> field.getJavaType().getImportName())
                .filter(type -> type != null)
                .distinct()
                .sorted()
                .map(type -> "import " + type + ";")
                .collect(Collectors.joining("\n"));
            configs.put("importTypes", types);
        }
        return configs;
    }
}
