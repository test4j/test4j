package org.jtester.module.database.transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.jtester.module.JTesterException;
import org.jtester.module.core.TestContext;
import org.jtester.module.core.utility.MessageHelper;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 默认事务管理类
 * 
 * @author darui.wudr
 */
public class DefaultTransactionManager implements TransactionManager {
    protected Map<Object, Boolean>                     testObjectTransactionActiveMap          = new HashMap<Object, Boolean>();

    /**
     * ThreadLocal for holding the TransactionStatus that keeps track of the
     * current test's transaction status
     */
    protected Map<Object, TransactionStatus>           testObjectTransactionStatusMap          = new HashMap<Object, TransactionStatus>();

    /**
     * ThreadLocal for holding the PlatformTransactionManager that is used by
     * the current test
     */
    protected Map<Object, PlatformTransactionManager>  testObjectPlatformTransactionManagerMap = new HashMap<Object, PlatformTransactionManager>();

    /**
     * Set of possible providers of a spring
     * <code>PlatformTransactionManager</code>, not null
     */
    protected List<TransactionManagementConfiguration> transactionManagementConfigurations;

    public void init(Set<TransactionManagementConfiguration> transactionManagementConfigurations) {
        setTransactionManagementConfigurations(transactionManagementConfigurations);
    }

    /**
     * Returns the given datasource, wrapped in a spring
     * <code>TransactionAwareDataSourceProxy</code>
     */
    public DataSource getTransactionalDataSource(DataSource dataSource) {
        return new TransactionAwareDataSourceProxy(dataSource);
    }

    public void startTransaction() {
        Object testObject = TestContext.currTestedObject();
        TransactionManagementConfiguration transactionManagementConfiguration = getTransactionManagementConfiguration(testObject);
        if (transactionManagementConfiguration.isTransactionalResourceAvailable(testObject)) {
            testObjectTransactionActiveMap.put(testObject, Boolean.TRUE);
            doStartTransaction(testObject, transactionManagementConfiguration);
        } else {
            testObjectTransactionActiveMap.put(testObject, Boolean.FALSE);
        }
    }

    public void activateTransactionIfNeeded() {
        Object testObject = TestContext.currTestedObject();
        Boolean hasActived = testObjectTransactionActiveMap.get(testObject);
        if (hasActived == Boolean.FALSE) {
            testObjectTransactionActiveMap.put(testObject, Boolean.TRUE);
            TransactionManagementConfiguration transactionManagementConfiguration = getTransactionManagementConfiguration(testObject);
            doStartTransaction(testObject, transactionManagementConfiguration);
        }
    }

    protected void doStartTransaction(Object testObject,
                                      TransactionManagementConfiguration transactionManagementConfiguration) {
        MessageHelper.debug("Starting transaction");
        PlatformTransactionManager platformTransactionManager = transactionManagementConfiguration
                .getSpringPlatformTransactionManager(testObject);
        testObjectPlatformTransactionManagerMap.put(testObject, platformTransactionManager);
        TransactionDefinition definition = createTransactionDefinition(testObject);
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(definition);
        testObjectTransactionStatusMap.put(testObject, transactionStatus);
    }

    public void commit() {
        Object testObject = TestContext.currTestedObject();
        Boolean hasActived = testObjectTransactionActiveMap.containsKey(testObject);
        if (hasActived == null) {
            throw new JTesterException("Trying to commit, while no transaction is currently active");
        }
        TransactionStatus transactionStatus = testObjectTransactionStatusMap.get(testObject);
        if (hasActived == Boolean.TRUE && transactionStatus != null) {
            MessageHelper.debug("Committing transaction");
            testObjectPlatformTransactionManagerMap.get(testObject).commit(transactionStatus);
        }
        removeTestedObjectTransactionManager(testObject);
    }

    public void rollback() {
        Object testObject = TestContext.currTestedObject();
        Boolean hasActived = testObjectTransactionActiveMap.containsKey(testObject);
        if (hasActived == null) {
            throw new JTesterException("Trying to rollback, while no transaction is currently active");
        }
        TransactionStatus transactionStatus = testObjectTransactionStatusMap.get(testObject);
        if (hasActived == Boolean.TRUE && transactionStatus != null) {
            MessageHelper.debug("Rolling back transaction");
            testObjectPlatformTransactionManagerMap.get(testObject).rollback(transactionStatus);
        }
        removeTestedObjectTransactionManager(testObject);
    }

    /**
     * 移除测试对象的事务管理对象
     * 
     * @param testObject
     */
    private void removeTestedObjectTransactionManager(Object testObject) {
        testObjectTransactionActiveMap.remove(testObject);
        testObjectTransactionStatusMap.remove(testObject);
        testObjectPlatformTransactionManagerMap.remove(testObject);
    }

    /**
     * Returns a <code>TransactionDefinition</code> object containing the
     * necessary transaction parameters. Simply returns a default
     * <code>DefaultTransactionDefinition</code> object with the 'propagation
     * required' attribute
     * 
     * @param testObject The test object, not null
     * @return The default TransactionDefinition
     */
    protected TransactionDefinition createTransactionDefinition(Object testObject) {
        return new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
    }

    protected TransactionManagementConfiguration getTransactionManagementConfiguration(Object testObject) {
        for (TransactionManagementConfiguration transactionManagementConfiguration : transactionManagementConfigurations) {
            if (transactionManagementConfiguration.isApplicableFor(testObject)) {
                return transactionManagementConfiguration;
            }
        }
        throw new JTesterException("No applicable transaction management configuration found for test "
                + testObject.getClass());
    }

    protected void setTransactionManagementConfigurations(Set<TransactionManagementConfiguration> transactionManagementConfigurationsSet) {
        List<TransactionManagementConfiguration> configurations = new ArrayList<TransactionManagementConfiguration>();
        configurations.addAll(transactionManagementConfigurationsSet);
        Collections.sort(configurations, new Comparator<TransactionManagementConfiguration>() {

            public int compare(TransactionManagementConfiguration o1, TransactionManagementConfiguration o2) {
                return o2.getPreference().compareTo(o1.getPreference());
            }

        });
        this.transactionManagementConfigurations = configurations;
    }
}
