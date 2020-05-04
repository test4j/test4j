package org.test4j.generator.mybatis.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.test4j.generator.mybatis.config.DataSourceConfig;
import org.test4j.generator.mybatis.config.IDbQuery;
import org.test4j.generator.mybatis.config.INameConvert;
import org.test4j.generator.mybatis.config.StrategyConfig;
import org.test4j.generator.mybatis.query.H2Query;
import org.test4j.generator.mybatis.rule.DbType;
import org.test4j.generator.mybatis.rule.IColumnType;
import lombok.experimental.Accessors;
import org.test4j.generator.mybatis.rule.Naming;
import org.test4j.tools.commons.StringHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

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
    private Map<FileType, String> fileTypeName = new HashMap<>();

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
        this.addImportTypes(TableName.class.getCanonicalName());
    }

    public TableInfo(String tableName, String entityPrefix) {
        this.tableName = tableName;
        this.entityPrefix = entityPrefix;
        this.addImportTypes(TableName.class.getCanonicalName());
    }

    TableInfo(String tableName, BuildConfig buildConfig) {
        this.tableName = tableName;
        this.buildConfig = buildConfig;
        this.addImportTypes(TableName.class.getCanonicalName());
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

    public TableInfo column(String columnName, IColumnType columnType) {
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
            this.gmtCreateField = field;
        } else if (this.gmtModified.equalsIgnoreCase(fieldName)) {
            this.gmtModifiedField = field;
        } else if (this.logicDeleted.equalsIgnoreCase(fieldName)) {
            this.addImportTypes(TableLogic.class.getCanonicalName());
            this.isDeletedField = field;
        } else if (field.isPrimary()) {
            this.addImportTypes(TableId.class.getCanonicalName());
            if (field.isAutoIncrement()) {
                this.addImportTypes(IdType.class.getCanonicalName());
            }
            this.primary = field;
        } else {
            this.addImportTypes(com.baomidou.mybatisplus.annotation.TableField.class.getCanonicalName());
            this.fields.add(field);
        }
        if (null != field.getColumnType().getImportName()) {
            this.addImportTypes(field.getColumnType().getImportName());
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
        INameConvert nameConvert = this.generator.getStrategyConfig().getNameConvert();
        if (null != nameConvert) {
            return nameConvert.entityNameConvert(this);
        } else {
            return Naming.capitalFirst(propertyName);
        }
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
        DataSourceConfig dbConfig = this.generator.getDataSourceConfig();
        DbType dbType = dbConfig.getDbType();
        IDbQuery dbQuery = dbConfig.getDbQuery();

        try {
            Set<String> h2PkColumns = this.h2PkColumns();
            String tableFieldsSql = this.buildTableFieldsSql();
            try (PreparedStatement preparedStatement = dbConfig.getConn().prepareStatement(tableFieldsSql); ResultSet results = preparedStatement.executeQuery()) {
                while (results.next()) {
                    this.initTableField(dbType, dbQuery, h2PkColumns, results);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception：" + e.getMessage(), e);
        }

        this.fields.sort(Comparator.comparing(TableField::getColumnName));
        this.fieldNames = fields.stream().map(TableField::getColumnName).collect(Collectors.joining(", "));

        return this;
    }

    private void initTableField(DbType dbType, IDbQuery dbQuery, Set<String> h2PkColumns, ResultSet results) throws SQLException {
        String columnName = results.getString(dbQuery.fieldName());
        if (this.isExclude(columnName)) {
            return;
        }
        TableField field = new TableField(columnName);
        boolean primary = this.isPrimary(columnName, results, h2PkColumns);
        // 处理ID, 避免多重主键设置，目前只取第一个找到ID，并放到list中的索引为0的位置
        if (primary && !haveId) {
            field.setPrimary(true);
            field.setAutoIncrement(DbType.H2 == dbType || DbType.SQLITE == dbType || dbQuery.isKeyIdentity(results));
            haveId = true;
        } else {
            field.setPrimary(false);
        }
        field.setFieldType(results.getString(dbQuery.fieldType()));
        field.initNaming(this, results);
        this.addField(field);
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

    String outputDir;

    public String getOutputDir() {
        if (outputDir == null) {
            this.outputDir = this.generator.getOutputDir() + '/' + this.generator.getBasePackage().replace('.', '/');
        }
        return this.outputDir;
    }

    public String getBasePackage() {
        return generator.getBasePackage();
    }
}
