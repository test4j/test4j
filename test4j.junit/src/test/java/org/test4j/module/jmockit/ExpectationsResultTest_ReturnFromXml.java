package org.test4j.module.jmockit;

import mockit.Mocked;

import org.junit.Test;
import org.test4j.fortest.beans.Manager;
import org.test4j.junit.Test4J;
import org.test4j.tools.commons.JSONHelper;

public class ExpectationsResultTest_ReturnFromXml implements Test4J {
    @Mocked
    private ManagerService service;

    @Test
    public void returnValue() {
        new Expectations() {
            {
                when(service.getManager(the.string().any().wanted())).thenReturnFrom(Manager.class, JSONHelper.class,
                        "manager.json");
            }
        };
        Manager manager = service.getManager("darui.wu");
        want.object(manager).propertyEq("name", "Tony Tester");
    }

    public static class ManagerService {
        public Manager getManager(String name) {
            return null;
        }
    }
}
