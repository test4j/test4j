package org.test4j.hamcrest.iassert.object.impl;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

import java.util.Arrays;

public class CollectionAssertTest_SizeMatch extends Test4J {

    @Test
    public void sizeIs() {
        want.collection(Arrays.asList(1, 2, 4)).sizeIs(3);
    }

    @Test
    public void sizeIs_1() {
        want.array(new int[]{1, 2, 3}).sizeEq(3);
    }

    @Test
    public void sizeIs_2() {
        want.exception(() ->
                        want.collection(Arrays.asList(1, 2, 4)).sizeIs(2),
                AssertionError.class);
    }

    @Test
    public void sizeIs_3() {
        want.exception(() ->
                        want.collection(Arrays.asList(1, 2, 4)).sizeIs(4),
                AssertionError.class);
    }

    @Test
    public void sizeGe() {
        want.collection(Arrays.asList(1, 2, 4)).sizeGe(2);
    }

    @Test
    public void sizeGe_2() {
        want.collection(Arrays.asList(1, 2, 4)).sizeGe(3);
    }

    @Test
    public void sizeGe_3() {
        want.exception(() ->
                        want.collection(Arrays.asList(1, 2, 4)).sizeGe(4),
                AssertionError.class);
    }

    @Test
    public void sizeGt() {
        want.exception(() ->
                        want.collection(Arrays.asList(1, 2, 4)).sizeGt(3),
                AssertionError.class);
    }

    @Test
    public void sizeGt_2() {
        want.collection(Arrays.asList(1, 2, 4)).sizeGt(2);
    }

    @Test
    public void sizeGt_3() {
        want.exception(() ->
                        want.collection(Arrays.asList(1, 2, 4)).sizeGt(4),
                AssertionError.class);
    }

    @Test
    public void sizeLe() {
        want.collection(Arrays.asList(1, 2, 4)).sizeLe(4);
    }

    @Test
    public void sizeLe_2() {
        want.collection(Arrays.asList(1, 2, 4)).sizeLe(3);
    }

    @Test
    public void sizeLe_3() {
        want.exception(() ->
                        want.collection(Arrays.asList(1, 2, 4)).sizeLe(2),
                AssertionError.class);
    }

    @Test
    public void sizeLt() {
        want.collection(Arrays.asList(1, 2, 4)).sizeLt(4);
    }

    @Test
    public void sizeLt_2() {
        want.exception(() ->
                        want.collection(Arrays.asList(1, 2, 4)).sizeLt(3),
                AssertionError.class);
    }

    @Test
    public void sizeLt_3() {
        want.exception(() ->
                        want.collection(Arrays.asList(1, 2, 4)).sizeLt(2),
                AssertionError.class);
    }

    @Test
    public void sizeNe() {
        want.array(new String[]{"", null, "ddd"}).sizeNe(4);
    }

    @Test
    public void sizeNe_2() {
        want.exception(() ->
                        want.array(new String[]{"", null, "ddd"}).sizeNe(3),
                AssertionError.class);
    }

    @Test
    public void sizeNe_3() {
        want.array(new String[]{"", null, "ddd"}).sizeNe(2);
    }
}
