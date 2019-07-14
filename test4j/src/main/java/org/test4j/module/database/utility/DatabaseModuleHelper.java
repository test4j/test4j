package org.test4j.module.database.utility;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.UncategorizedScriptException;
import org.springframework.util.Assert;
import org.test4j.module.core.utility.ModulesManager;
import org.test4j.module.database.DatabaseModule;
import org.test4j.module.IScript;
import org.test4j.tools.commons.ClazzHelper;
import org.test4j.tools.commons.ConfigHelper;
import org.test4j.tools.commons.StringHelper;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.jdbc.datasource.init.ScriptUtils.executeSqlScript;

/**
 * Class providing access to the functionality of the database module using
 * static methods. Meant to be used directly in unit tests.
 */
public class DatabaseModuleHelper {
    static Map<String, Boolean> HAS_INIT_DATASOURCE_SCRIPT = new HashMap<>();

    /**
     * Gets the instance DatabaseModule that is registered in the modules
     * repository. This instance implements the actual behavior of the static
     * methods in this class. This way, other implementations can be plugged in,
     * while keeping the simplicity of using static methods.
     *
     * @return the instance, not null
     */
    public static DatabaseModule getDatabaseModule() {
        return ModulesManager.getModuleInstance(DatabaseModule.class);
    }

    public static void runInitScripts(DataSource dataSource, String beanName) {
        if (dataSource == null) {
            return;
        }
        if (HAS_INIT_DATASOURCE_SCRIPT.containsKey(beanName)) {
            return;
        } else {
            String factory = ConfigHelper.getDataSourceKey(beanName, "script.factory");
            if (StringHelper.isBlankOrNull(factory)) {
                runFromClasspathResource(dataSource, beanName);
            } else {
                runFromScriptFactory(dataSource, factory);
            }
            HAS_INIT_DATASOURCE_SCRIPT.put(beanName, true);
        }
    }

    private static void runFromScriptFactory(DataSource dataSource, String factory) {
        try {
            Object instance = ClazzHelper.createInstanceOfType(factory);
            if (!(instance instanceof IScript)) {
                throw new RuntimeException("the script class should implement interface: " + IScript.class.getName());
            }
            String script = ((IScript) instance).script();
            Assert.notNull(script, "script can't be null.");
            Connection connection = DataSourceUtils.getConnection(dataSource);
            try {
                Resource resource = new InputStreamResource(new ByteArrayInputStream(script.getBytes()));
                executeSqlScript(connection, new EncodedResource(resource, "utf-8"), false, true, "--", ";", "/*", "*/");
            } finally {
                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        } catch (ScriptException e) {
            throw e;
        } catch (Exception e) {
            throw new UncategorizedScriptException("Failed to execute database script", e);
        }
    }

    private static void runFromClasspathResource(DataSource dataSource, String beanName) {
        List<Resource> resources = getResources(beanName);
        if (resources.isEmpty()) {
            return;
        }
        Assert.notNull(dataSource, "DataSource must be provided.");
        try {
            Connection connection = DataSourceUtils.getConnection(dataSource);
            Assert.notNull(connection, "connection must be null.");
            try {
                for (Resource resource : resources) {
                    executeSqlScript(connection, new EncodedResource(resource, "utf-8"), false, true, "--", ";", "/*", "*/");
                }
            } finally {
                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        } catch (ScriptException e) {
            throw e;
        } catch (Exception e) {
            throw new UncategorizedScriptException("Failed to execute database script", e);
        }
    }

    private static List<Resource> getResources(String beanName) {
        String[] locations = ConfigHelper.getDataSourceKey(beanName, "script").split("[;,]");
        List<Resource> resources = new ArrayList<>();
        for (String location : locations) {
            if (StringHelper.isBlankOrNull(location)) {
                continue;
            }
            Resource resource = new ClassPathResource(location);
            if (resource.exists()) {
                resources.add(resource);
            } else {
                throw new RuntimeException("The specified resource[" + beanName + "] does not exist.");
            }
        }
        return resources;
    }
}
