package org.jtester.module.jmockit;

import mockit.Mocked;

import org.jtester.fortest.beans.Manager;
import org.jtester.junit.JTester;
import org.jtester.tools.commons.JSONHelper;
import org.junit.Test;

public class ExpectationsResultTest_ReturnFromXml implements JTester {
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
