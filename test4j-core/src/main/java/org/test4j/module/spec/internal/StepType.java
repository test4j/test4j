package org.test4j.module.spec.internal;

import org.test4j.tools.commons.StringHelper;

import java.lang.annotation.Annotation;

public enum StepType {
    Step {
        @Override
        public Class<? extends Annotation> getAnnotationClass() {
            return org.test4j.module.spec.annotations.Step.class;
        }
    },
    Given {
        @Override
        public Class<? extends Annotation> getAnnotationClass() {
            return org.test4j.module.spec.annotations.Given.class;
        }
    },
    When {
        @Override
        public Class<? extends Annotation> getAnnotationClass() {
            return org.test4j.module.spec.annotations.When.class;
        }
    },
    Then {
        @Override
        public Class<? extends Annotation> getAnnotationClass() {
            return org.test4j.module.spec.annotations.Then.class;
        }
    };

    public abstract Class<? extends Annotation> getAnnotationClass();

    public static StepType getStepType(String type) {
        if (StringHelper.isBlank(type)) {
            return Step;
        }
        if ("given".equalsIgnoreCase(type)) {
            return Given;
        }
        if ("when".equalsIgnoreCase(type)) {
            return When;
        }
        if ("then".equalsIgnoreCase(type)) {
            return Then;
        }
        throw new RuntimeException("the step type can only be following values: Given, When and Then; but actual is "
                + type);
    }
}