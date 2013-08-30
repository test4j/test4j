package org.jtester.module.jmockit;

import org.jtester.module.jmockit.extend.JMockitExpectations;

/**
 * @author darui.wudr 2013-1-15 上午10:12:14
 */
@SuppressWarnings("rawtypes")
public interface IMockict {
    class Expectations extends JMockitExpectations {

        public Expectations() {
            super();
        }

        public Expectations(int numberOfIterations, Object... classesOrObjectsToBePartiallyMocked) {
            super(numberOfIterations, classesOrObjectsToBePartiallyMocked);
        }

        public Expectations(Object... classesOrObjectsToBePartiallyMocked) {
            super(classesOrObjectsToBePartiallyMocked);
        }

    }

    class NonStrictExpectations extends org.jtester.module.jmockit.extend.JMockitNonStrictExpectations {

        public NonStrictExpectations() {
            super();
        }

        public NonStrictExpectations(int numberOfIterations, Object... classesOrObjectsToBePartiallyMocked) {
            super(numberOfIterations, classesOrObjectsToBePartiallyMocked);
        }

        public NonStrictExpectations(Object... classesOrObjectsToBePartiallyMocked) {
            super(classesOrObjectsToBePartiallyMocked);
        }
    }

    class MockUp<T> extends mockit.MockUp<T> {

        public MockUp() {
            super();
        }

        public MockUp(Class classToMock) {
            super(classToMock);
        }
    }
}
