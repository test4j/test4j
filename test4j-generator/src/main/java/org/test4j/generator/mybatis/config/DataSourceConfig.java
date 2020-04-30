package org.test4j.generator.mybatis.config;

import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import org.test4j.generator.mybatis.convert.*;
import org.test4j.generator.mybatis.query.*;
import org.test4j.generator.mybatis.rule.DbType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库配置
 *
 * @author YangHu
 * @since 2016/8/30
 */
@Data
@Accessors(chain = true)
public class DataSourceConfig {

    /**
     * 数据库信息查询
     */
    private IDbQuery dbQuery;
    /**
     * 数据库类型
     */
    private DbType dbType;
    /**
     * PostgreSQL schemaName
     */
    private String schemaName;
    /**
     * 类型转换
     */
    private ITypeConvert typeConvert;
    /**
     * 驱动连接的URL
     */
    private String url;
    /**
     * 驱动名称
     */
    private String driverName;
    /**
     * 数据库连接用户名
     */
    private String username;
    /**
     * 数据库连接密码
     */
    private String password;

    public IDbQuery getDbQuery() {
        if (null == dbQuery) {
            switch (getDbType()) {
                case ORACLE:
                    dbQuery = new OracleQuery();
                    break;
                case SQL_SERVER:
                    dbQuery = new SqlServerQuery();
                    break;
                case POSTGRE_SQL:
                    dbQuery = new PostgreSqlQuery();
                    break;
                case DB2:
                    dbQuery = new DB2Query();
                    break;
                case MARIADB:
                    dbQuery = new MariadbQuery();
                    break;
                case H2:
                    dbQuery = new H2Query();
                    break;
                case SQLITE:
                    dbQuery = new SqliteQuery();
                    break;
                default:
                    // 默认 MYSQL
                    dbQuery = new MySqlQuery();
                    break;
            }
        }
        return dbQuery;
    }

    /**
     * 判断数据库类型
     *
     * @return 类型枚举值
     */
    public DbType getDbType() {
        if (null == this.dbType) {
            this.dbType = DbType.getDbType(this.driverName);
            if (null == this.dbType) {
                this.dbType = DbType.getDbType(this.url.toLowerCase());
            }
            if (null == this.dbType) {
                throw ExceptionUtils.mpe("Unknown type of database!");
            }
        }
        return this.dbType;
    }

    public ITypeConvert getTypeConvert() {
        if (null == typeConvert) {
            switch (getDbType()) {
                case ORACLE:
                    typeConvert = new OracleTypeConvert();
                    break;
                case SQL_SERVER:
                    typeConvert = new SqlServerTypeConvert();
                    break;
                case POSTGRE_SQL:
                    typeConvert = new PostgreSqlTypeConvert();
                    break;
                case DB2:
                    typeConvert = new DB2TypeConvert();
                    break;
                case SQLITE:
                    typeConvert = new SqliteTypeConvert();
                    break;
                case MARIADB:
                default:
                    typeConvert = new MySqlTypeConvert();
                    break;
            }
        }
        return typeConvert;
    }

    /**
     * 创建数据库连接对象
     *
     * @return Connection
     */
    public Connection getConn() {
        Connection conn = null;
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
