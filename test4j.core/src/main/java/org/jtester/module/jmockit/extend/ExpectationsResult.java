package org.jtester.module.jmockit.extend;

import java.io.File;

import mockit.internal.expectations.transformation.ActiveInvocations;

import org.jtester.tools.commons.ClazzHelper;
import org.jtester.tools.commons.JSONHelper;

@SuppressWarnings({ "rawtypes" })
public class ExpectationsResult {
    protected JTesterInvocations currExpectations;

    protected ExpectationsResult(JTesterInvocations expectations) {
        this.currExpectations = expectations;
    }

    public void thenReturn(Object o) {
        if (o instanceof Throwable) {
            currExpectations.thenReturn(o);
        } else {
            ActiveInvocations.addResult(o);
        }
    }

    /**
     * return value parsed from xml file
     * 
     * @param claz the class of the return object
     * @param json
     */
    public void thenReturnFrom(Class claz, String json) {
        Object o = JSONHelper.fromJsonFile(claz, json);
        thenReturn(o);
    }

    /**
     * return value parsed from xml file
     * 
     * @param claz the class of the return object
     * @param path class path of the xml file
     * @param json
     */
    public void thenReturnFrom(Class claz, Class clazPath, String json) {
        String path = ClazzHelper.getPathFromPath(clazPath);
        Object o = JSONHelper.fromJsonFile(claz, path + File.separatorChar + json);
        thenReturn(o);
    }

    public void thenReturn(Object o, Object... os) {
        thenReturn(o);
        for (Object o1 : os) {
            thenReturn(o1);
        }
    }

    public void thenThrows(Throwable e) {
        ActiveInvocations.addResult(e);
    }

    public void thenThrows(Throwable e, Throwable... es) {
        thenThrows(e);
        for (Throwable e1 : es) {
            thenThrows(e1);
        }
    }
}
