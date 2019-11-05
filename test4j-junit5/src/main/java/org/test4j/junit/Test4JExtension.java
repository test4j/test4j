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

package org.test4j.junit;

import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContextManager;
import org.test4j.junit.web.SpringJUnitJupiterWebConfig;
import org.test4j.module.core.internal.Test4JContext;
import org.test4j.module.core.utility.JMockitHelper;
import org.test4j.module.spring.interal.SpringEnv;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.test4j.junit.SpringExtensionHelper.*;
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
 * @see SpringJUnitJupiterConfig
 * @see SpringJUnitJupiterWebConfig
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

    /**
     * Determine if the value for the {@link Parameter} in the supplied {@link ParameterContext}
     * should be autowired from the test's {@link ApplicationContext}.
     * <p>Returns {@code true} if the parameter is declared in a {@link Constructor}
     * that is annotated with {@link Autowired @Autowired} and otherwise delegates to
     * {@link ParameterAutowireUtils#isAutowirable}.
     * <p><strong>WARNING</strong>: If the parameter is declared in a {@code Constructor}
     * that is annotated with {@code @Autowired}, Spring will assume the responsibility
     * for resolving all parameters in the constructor. Consequently, no other registered
     * {@link ParameterResolver} will be able to resolve parameters.
     *
     * @see #resolveParameter
     * @see ParameterAutowireUtils#isAutowirable
     */
//    @Override
//    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
//        Parameter parameter = parameterContext.getParameter();
//        Executable executable = parameter.getDeclaringExecutable();
//        return (executable instanceof Constructor &&
//                AnnotatedElementUtils.hasAnnotation(executable, Autowired.class)) ||
//                ParameterAutowireUtils.isAutowirable(parameter);
//    }

    /**
     * Resolve a value for the {@link Parameter} in the supplied {@link ParameterContext} by
     * retrieving the corresponding dependency from the test's {@link ApplicationContext}.
     * <p>Delegates to {@link ParameterAutowireUtils#resolveDependency}.
     *
     * @see #supportsParameter
     * @see ParameterAutowireUtils#resolveDependency
     */
//    @Override
//    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
//        Parameter parameter = parameterContext.getParameter();
//        Class<?> testClass = extensionContext.getRequiredTestClass();
//        ApplicationContext applicationContext = getApplicationContext(extensionContext);
//        return ParameterAutowireUtils.resolveDependency(parameter, testClass, applicationContext);
//    }
}
