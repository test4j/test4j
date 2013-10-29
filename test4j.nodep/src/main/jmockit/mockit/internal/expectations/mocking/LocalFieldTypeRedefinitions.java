/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.mocking;

import mockit.internal.expectations.RecordAndReplayExecution;
import mockit.internal.state.TestRun;
import mockit.internal.util.FieldReflection;

public final class LocalFieldTypeRedefinitions extends FieldTypeRedefinitions {
    private final RecordAndReplayExecution execution;

    public LocalFieldTypeRedefinitions(Object objectWithMockFields, RecordAndReplayExecution execution) {
        super(objectWithMockFields);
        this.execution = execution;
    }

    public void redefineLocalFieldTypes() {
        redefineFieldTypes(parentObject.getClass(), false);
    }

    @Override
    protected void redefineTypeForMockField() {
        TypeRedefinition typeRedefinition = new TypeRedefinition(parentObject, typeMetadata);

        if (finalField) {
            typeRedefinition.redefineTypeForFinalField();
            registerMockedClassIfNonStrict();

            if (typeMetadata.getMaxInstancesToCapture() > 0) {
                execution.addMockedTypeToMatchOnInstance(typeRedefinition.targetClass);
            }

            TestRun.getExecutingTest().addFinalLocalMockField(parentObject, typeMetadata);
        } else {
            Object mock = typeRedefinition.redefineType().create();
            FieldReflection.setFieldValue(field, parentObject, mock);
            registerMock(mock);
        }

        execution.addLocalMock(typeMetadata.declaredType, parentObject);
        addTargetClass();

        //remove by davey.wu 2013-10-29
        //      System.out.println(
        //         "WARNING: Local mock field \"" + typeMetadata.mockId +
        //         "\" should be moved to the test class or converted to a parameter of the test method");
        //      System.out.println("  at " + new StackTrace().findPositionInTestMethod());
        // end remove by davey.wu
    }

    @Override
    public boolean captureNewInstanceForApplicableMockField(Object mock) {
        return captureOfNewInstances != null
                && getCaptureOfNewInstances().captureNewInstanceForApplicableMockField(parentObject, mock);
    }
}
