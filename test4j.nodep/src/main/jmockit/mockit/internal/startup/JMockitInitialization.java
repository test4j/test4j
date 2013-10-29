/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.startup;

import java.io.IOException;

import jmockit.test4j.JUnit4BuilderDecorator;

import mockit.MockUp;
import mockit.integration.junit3.internal.MockTestCase;
import mockit.integration.junit3.internal.TestSuiteDecorator;
import mockit.integration.junit4.internal.BlockJUnit4ClassRunnerDecorator;
import mockit.integration.junit4.internal.MockFrameworkMethod;
import mockit.integration.junit4.internal.RunNotifierDecorator;
import mockit.integration.testng.internal.MockTestNG;
import mockit.internal.MockingBridge;
import mockit.internal.mockups.ClassStubbing;
import mockit.internal.mockups.MockClassSetup;
import mockit.internal.util.ClassLoad;
import mockit.internal.util.StackTrace;


final class JMockitInitialization {
    private final StartupConfiguration config;

    JMockitInitialization() throws IOException {
        config = new StartupConfiguration();
    }

    void initialize(boolean initializeTestNG) {
        MockingBridge.preventEventualClassLoadingConflicts();
        loadInternalStartupMocksForJUnitIntegration();

        if (initializeTestNG && MockTestNG.hasDependenciesInClasspath()) {
            setUpInternalStartupMock(MockTestNG.class);
        }

        loadExternalToolsIfAny();
        stubOutClassesIfAny();
        setUpStartupMocksIfAny();
    }

    private void loadInternalStartupMocksForJUnitIntegration() {
        if (setUpInternalStartupMock(TestSuiteDecorator.class)) {
            try {
                setUpInternalStartupMock(MockTestCase.class);
            } catch (VerifyError ignore) {
                // For some reason, this error occurs when running TestNG tests from Maven.
            }

            if (!setUpInternalStartupMock(RunNotifierDecorator.class))
                return;
            if (!setUpInternalStartupMock(BlockJUnit4ClassRunnerDecorator.class))
                return;
            setUpInternalStartupMock(MockFrameworkMethod.class);
            /** added by davey.wu **/
            setUpInternalStartupMock(JUnit4BuilderDecorator.class);
            /** end added by davey.wu **/
        }
    }

    private boolean setUpInternalStartupMock(Class<? extends MockUp<?>> mockClass) {
        // The startup mock is ignored if the necessary third-party class files are not in the classpath.
        try {
            new MockClassSetup(mockClass).redefineMethods();
            return true;
        } catch (TypeNotPresentException ignore) {
            return false;
        } catch (NoClassDefFoundError ignore) {
            return false;
        }
    }

    private void loadExternalToolsIfAny() {
        for (String toolClassName : config.externalTools) {
            try {
                new ToolLoader(toolClassName).loadTool();
            } catch (Throwable unexpectedFailure) {
                StackTrace.filterStackTrace(unexpectedFailure);
                unexpectedFailure.printStackTrace();
            }
        }
    }

    private void stubOutClassesIfAny() {
        for (String realClassName : config.classesToBeStubbedOut) {
            Class<?> realClass = ClassLoad.loadClass(realClassName.trim());
            new ClassStubbing(realClass).stubOutAtStartup();
        }
    }

    private void setUpStartupMocksIfAny() {
        for (String mockClassName : config.mockClasses) {
            try {
                Class<?> mockClass = ClassLoad.loadClass(mockClassName);

                if (MockUp.class.isAssignableFrom(mockClass)) {
                    @SuppressWarnings("unchecked")
                    Class<MockUp<?>> mockUpSubclass = (Class<MockUp<?>>) mockClass;
                    new MockClassSetup(mockUpSubclass).redefineMethods();
                }
            } catch (UnsupportedOperationException ignored) {
            } catch (Throwable unexpectedFailure) {
                StackTrace.filterStackTrace(unexpectedFailure);
                unexpectedFailure.printStackTrace();
            }
        }
    }
}
