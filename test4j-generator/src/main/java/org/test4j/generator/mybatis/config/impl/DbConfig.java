package org.test4j.generator.mybatis.config.impl;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.test4j.generator.mybatis.db.DbType;
import org.test4j.generator.mybatis.db.IDbQuery;
import org.test4j.generator.mybatis.db.ITypeConvert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库配置
 *
 * @author darui.wu
 */
@Data
@Accessors(chain = true)
public class DbConfig {
    /**
     * 数据库类型
     */
    private DbType dbType;
    /**
     * 驱动名称
     */
    private String driverName;
    /**
     * 驱动连接的URL
     */
    private String url;
    /**
     * 数据库连接用户名
     */
    private String username;
    /**
     * 数据库连接密码
     */
    private String password;
    /**
     * schemaName
     */
    private String schemaName;


    public DbConfig() {
    }

    public DbConfig(DbType dbType, String driverName, String url, String username, String password) {
        if (url == null) {
            throw new RuntimeException("请设置数据库链接信息 url");
        }
        this.dbType = dbType;
        this.driverName = driverName;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * 数据库信息查询
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private IDbQuery dbQuery;

    public IDbQuery getDbQuery() {
        if (null == dbQuery) {
            this.dbQuery = this.dbType.newQuery();
        }
        return dbQuery;
    }

    /**
     * 类型转换
     */
    @Getter(AccessLevel.NONE)
    private ITypeConvert typeConvert;

    public ITypeConvert getTypeConvert() {
        if (null == typeConvert) {
            this.typeConvert = dbType.newConvert();
        }
        return typeConvert;
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
                throw new RuntimeException("Unknown type of database!");
            }
        }
        return this.dbType;
    }

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Connection connection;

    /**
     * 创建数据库连接对象
     *
     * @return Connection
     */
    public Connection getConn() {
        if (connection == null) {
            try {
                Class.forName(driverName);
                return DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException("getConn error:" + e.getMessage(), e);
            }
        }
        return this.connection;
    }
}