package org.jtester.module.database.environment;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.jtester.module.JTesterException;
import org.jtester.module.core.TestContext;
import org.jtester.module.core.utility.MessageHelper;
import org.jtester.module.database.annotations.Transactional.TransactionMode;
import org.jtester.module.database.environment.typesmap.AbstractTypeMap;
import org.jtester.module.database.transaction.DefaultTransactionManager;
import org.jtester.module.database.transaction.TransactionManagementConfiguration;
import org.jtester.module.database.transaction.TransactionManager;
import org.jtester.module.database.utility.DataSourceType;
import org.jtester.tools.commons.ExceptionWrapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;

public abstract class BaseEnvironment implements DBEnvironment {

    /**
     * Set of possible providers of a spring
     * <code>PlatformTransactionManager</code>
     */
    protected Set<TransactionManagementConfiguration> transactionManagementConfigurations = new HashSet<TransactionManagementConfiguration>();

    protected final String                            dataSourceName;

    protected final String                            dataSourceFrom;

    protected DataSourceType                          dataSourceType;

    private JTesterDataSource                         dataSource;

    protected AbstractTypeMap                         typeMap;

    protected BaseEnvironment(DataSourceType dataSourceType, String dataSourceName, String dataSourceFrom) {
        this.dataSourceName = dataSourceName;
        this.dataSourceFrom = dataSourceFrom;
        this.dataSourceType = dataSourceType;
    }

    public void setDataSource(String driver, String url, String schemas, String username, String password) {
        this.dataSource = new JTesterDataSource(dataSourceType, driver, url, schemas, username, password);
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    /**
     * Returns the <code>DataSource</code> that provides connection to the unit
     * test database. When invoked the first time, the DBMaintainer is invoked
     * to make sure the test database is up-to-date (if database updating is
     * enabled)
     * 
     * @return The <code>DataSource</code>
     */
    public DataSource getDataSourceAndActivateTransactionIfNeeded() {
        ThreadTransactionManager currMethodConnection = threadTransactionManager.get();
        if (currMethodConnection != null) {
            currMethodConnection.activateTransaction();
        }
        return dataSource;
    }

    public void registerTransactionManagementConfiguration(TransactionManagementConfiguration transactionManagementConfiguration) {
        if (transactionManagementConfiguration == null) {
            transactionManagementConfigurations.add(new TransactionManagementConfiguration() {
                public boolean isApplicableFor(Object testObject) {
                    return true;
                }

                public PlatformTransactionManager getSpringPlatformTransactionManager(Object testObject) {
                    DataSource dataSource = getDataSourceAndActivateTransactionIfNeeded();
                    return new DataSourceTransactionManager(dataSource);
                }

                public boolean isTransactionalResourceAvailable(Object testObject) {
                    return true;
                }

                public Integer getPreference() {
                    return 1;
                }
            });
        } else {
            transactionManagementConfigurations.add(transactionManagementConfiguration);
        }
    }

    public void startTransaction() {
        this.threadTransactionManager.set(new ThreadTransactionManager());
        this.threadTransactionManager.get().startTransaction();
    }

    public void endTransaction() {
        ThreadTransactionManager currTransactionManager = this.threadTransactionManager.get();
        if (currTransactionManager != null) {
            currTransactionManager.endTransaction();
            this.threadTransactionManager.remove();
        }
    }

    /**
     * 当前线程的连接
     * 
     * @return
     */
    public Connection connect() {
        ThreadTransactionManager currMethodConnection = threadTransactionManager.get();
        if (currMethodConnection == null) {
            currMethodConnection = new ThreadTransactionManager();
            threadTransactionManager.set(currMethodConnection);
        }
        Connection connection = currMethodConnection.getConnection();
        if (connection != null) {
            return connection;
        }
        DataSource dataSource = this.getDataSourceAndActivateTransactionIfNeeded();
        try {
            connection = currMethodConnection.initMethodConnection(dataSource);
            return connection;
        } catch (SQLException e) {
            throw new JTesterException(e);
        }
    }

    /**
     * 事务的线程管理，在大部分情况下，测试只有一个线程。但有少数情况下例外(比如多线程测试)!
     */
    private final ThreadLocal<ThreadTransactionManager> threadTransactionManager = new ThreadLocal<ThreadTransactionManager>();

    private class ThreadTransactionManager {
        /**
         * The transaction manager
         */
        private TransactionManager transactionManager;

        private Object             testedObject;

        private Method             testedMethod;

        private Connection         connection;

        public Connection getConnection() {
            if (this.testedObject != TestContext.currTestedObject()
                    || this.testedMethod != TestContext.currTestedMethod()) {
                this.connection = null;
            }
            return connection;
        }

        public Connection initMethodConnection(DataSource dataSource) throws SQLException {
            this.testedObject = TestContext.currTestedObject();
            this.testedMethod = TestContext.currTestedMethod();
            connection = DataSourceUtils.doGetConnection(dataSource);
            return connection;
        }

        public void release() {
            if (this.connection == null) {
                return;
            }
            DataSource dataSource = getDataSourceAndActivateTransactionIfNeeded();
            try {
                if (connection.isClosed() == false) {
                    DataSourceUtils.doReleaseConnection(connection, dataSource);
                }
                this.connection = null;
            } catch (SQLException e) {
                throw new JTesterException(String.format("close datasource[%s] connection error.",
                        dataSource.toString()), e);
            }
        }

        boolean hasActivated = false;

        public void activateTransaction() {
            if (hasActivated == false && transactionManager != null) {
                transactionManager.activateTransactionIfNeeded();
                this.hasActivated = true;
            }
        }

        /**
         * start transaction<br>
         * if transaction manager does not exist yet, then create one.
         */
        public void startTransaction() {
            if (transactionManager == null) {
                transactionManager = new DefaultTransactionManager();
                transactionManager.init(transactionManagementConfigurations);
            }
            this.transactionManager.startTransaction();
        }

        public void endTransaction() {
            if (this.transactionManager == null) {
                this.release();
            } else {
                TransactionMode mode = DBEnvironmentFactory.getTransactionMode();
                if (mode == TransactionMode.COMMIT) {
                    this.commit();
                } else if (mode == TransactionMode.ROLLBACK) {
                    this.rollback();
                }
            }
        }

        public void rollback() {
            if (this.transactionManager != null) {
                this.transactionManager.rollback();
            }
            this.release();
        }

        public void commit() {
            if (this.transactionManager != null) {
                transactionManager.commit();
            }
            this.release();
        }
    }

    /**
     * 是否是默认的数据源
     * 
     * @return
     */
    public boolean isDefaultDBEnvironment() {
        boolean isDefault = DEFAULT_DATASOURCE_NAME.equals(this.dataSourceName)
                && DEFAULT_DATASOURCE_FROM.equals(dataSourceFrom);
        return isDefault;
    }

    /**
     * any processing required to turn a string into something jdbc driver can
     * process, can be used to clean up CRLF, externalise parameters if required
     * etc.
     */
    protected String parseCommandText(String commandText, String[] vars) {
        return commandText;
    }

    public void commit() {
        ThreadTransactionManager currTransactionManager = threadTransactionManager.get();
        if (currTransactionManager != null) {
            currTransactionManager.commit();
        }
    }

    public void rollback() {
        ThreadTransactionManager currTransactionManager = threadTransactionManager.get();
        if (currTransactionManager != null) {
            currTransactionManager.rollback();
        }
    }

    public int getExceptionCode(SQLException dbException) {
        return dbException.getErrorCode();
    }

    /**
     * by default, this is set to false.
     * 
     * @see org.jtester.module.database.environment.DBEnvironment#supportsOuputOnInsert()
     */
    public boolean supportsOuputOnInsert() {
        return false;
    }

    public PreparedStatement createStatementWithBoundFixtureSymbols(String commandText) throws SQLException {
        Connection connection = this.connect();
        PreparedStatement cs = connection.prepareStatement(commandText);
        return cs;
    }

    private Map<String, TableMeta> metas = new HashMap<String, TableMeta>();

    /**
     * 获得数据表的元信息
     * 
     * @param table
     * @return
     * @throws Exception
     */
    public TableMeta getTableMetaData(String table) {
        TableMeta meta = metas.get(table);
        if (meta == null) {
            try {
                String query = "select * from " + table + " where 1!=1";
                PreparedStatement st = this.createStatementWithBoundFixtureSymbols(query);
                ResultSet rs = st.executeQuery();

                meta = new TableMeta(table, rs.getMetaData(), this);
                metas.put(table, meta);
            } catch (Exception e) {
                throw ExceptionWrapper.getUndeclaredThrowableExceptionCaused(e);
            }
        }
        return meta;
    }

    public Object getDefaultValue(String javaType) {
        Object value = this.typeMap.getDefaultValue(javaType);
        return value;
    }

    public Object toObjectValue(String input, String javaType) {
        try {
            Object value = this.typeMap.toObjectByType(input, javaType);
            return value;
        } catch (Exception e) {
            MessageHelper.info("convert input[" + input + "] to type[" + javaType + "] error, so return input value.\n"
                    + e.getMessage());
            return input;
        }
    }

    /**
     * {@inheritDoc} <br>
     * <br>
     */
    @SuppressWarnings("rawtypes")
    public Object converToSqlValue(Object value) {
        if (value instanceof Enum) {
            return ((Enum) value).name();
        }
        return value;
    }
}
