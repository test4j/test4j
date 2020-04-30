package org.test4j.json.interal;

import org.test4j.json.impl.GsonImpl;
import org.test4j.tools.commons.ClazzHelper;
import org.test4j.tools.commons.ConfigHelper;
import org.test4j.tools.commons.StringHelper;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * JSONFactory
 *
 * @author wudarui
 */
public class JSONFactory {
    private static JSONInterface instance = null;

    private static Lock lock = new ReentrantLock();

    public static JSONInterface instance() {
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = createJsonInterface();
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    private static JSONInterface createJsonInterface() {
        String klassName = ConfigHelper.getString("test4j.json");
        if (StringHelper.isBlank(klassName)) {
            klassName = GsonImpl.class.getName();
        }
        return ClazzHelper.createInstanceOfType(klassName);
    }
}
