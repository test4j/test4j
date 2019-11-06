/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.test4j.junit5;

import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.TestContextManager;
import org.test4j.module.core.internal.Test4JContext;
import org.test4j.module.spring.interal.SpringEnv;

import java.lang.reflect.Method;

import static org.test4j.junit5.JUnit5Helper.*;
import static org.test4j.module.core.CoreModule.getTestListener;

/**
 * {@code SpringExtension} integrates the <em>Spring TestContext Framework</em>
 * into JUnit 5's <em>Jupiter</em> programming model.
 *
 * <p>To use this extension, simply annotate a JUnit Jupiter based test class with
 * {@code @ExtendWith(SpringExtension.class)}, {@code @SpringJUnitJupiterConfig}, or
 * {@code @SpringJUnitJupiterWebConfig}.
 *
 * @author Sam Brannen
 * @see TestContextManager
 * @since 5.0
 */
public class Test4JExtension implements
        BeforeAllCallback,
        AfterAllCallback,
        TestInstancePostProcessor,
        BeforeEachCallback,
        AfterEachCallback {

    /**
     * Delegates to {@link TestContextManager#beforeTestClass}.
     */
    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        Test4JContext.setContext(context.getRequiredTestClass());
        getTestListener().beforeClass(context.getRequiredTestClass());
        if (SpringEnv.isSpringEnv(context.getRequiredTestClass())) {
            getTestContextManager(context).beforeTestClass();
        }
    }

    /**
     * Delegates to {@link TestContextManager#afterTestClass}.
     */
    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        try {
            getTestContextManager(context).afterTestClass();
            getTestListener().afterClass(context.getRequiredTestClass());
        } finally {
            getStore(context).remove(context.getRequiredTestClass());
        }
    }

    /**
     * Delegates to {@link TestContextManager#prepareTestInstance}.
     */
    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        Test4JContext.setContext(testInstance, null);
        SpringEnv.setSpringEnv(context.getRequiredTestClass());
        if (SpringEnv.isSpringEnv(context.getRequiredTestClass())) {
            doSpringInitial(testInstance, context);
        }
    }


    /**
     * Delegates to {@link TestContextManager#beforeTestMethod}.
     */
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Object testInstance = context.getRequiredTestInstance();
        Method testMethod = context.getRequiredTestMethod();
        getTestListener().beforeMethod(testInstance, testMethod);
        if (SpringEnv.isSpringEnv(context.getRequiredTestClass())) {
            getTestContextManager(context).beforeTestMethod(testInstance, testMethod);
        }
    }

    /**
     * Delegates to {@link TestContextManager#afterTestMethod}.
     */
    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Object testInstance = context.getRequiredTestInstance();
        Method testMethod = context.getRequiredTestMethod();
        Throwable testException = context.getExecutionException().orElse(null);
        if (SpringEnv.isSpringEnv(context.getRequiredTestClass())) {
            getTestContextManager(context).afterTestMethod(testInstance, testMethod, testException);
        }
        getTestListener().afterMethod(testInstance, testMethod, testException);
    }
}
