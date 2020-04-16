package org.test4j.hamcrest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.junit5.Test4J;
import org.test4j.model.Address;
import org.test4j.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TestReflectionAssert extends Test4J {
    @Test
    public void test1() {
        User user1 = new User(1, "John", "Doe");
        User user2 = new User(1, "John", "Doe");
        want.exception(() ->
                        Assertions.assertEquals(user1, user2)
                , AssertionError.class);
    }

    @Test
    public void test2() {
        User user1 = new User(1, "John", "Doe");
        User user2 = new User(1, "John", "Doe");
        want.object(user1).eqReflect(user2);
    }

    @Test
    public void test3() {
        User user1 = new User(1, "John", "Doe");
        User user2 = new User(1, "John", "Doe1");
        want.exception(() ->
                        want.object(user1).eqReflect(user2)
                , AssertionError.class);
    }

    @Test
    public void test4() {
        want.object(1).eqReflect(1L);

        List<Double> myList = new ArrayList<Double>();
        myList.add(1.0);
        myList.add(2.0);
        want.exception(() -> want.object(myList).eqReflect(Arrays.asList(1, 2))
                , AssertionError.class).contains(new String[]{
                "expect=(Integer)", "actual=(Double)"
        });

    }

    @Test
    public void test5() {
        List<Integer> myList = Arrays.asList(3, 2, 1);
        want.object(myList).eqReflect(Arrays.asList(1, 2, 3), EqMode.IGNORE_ORDER);

        User actualUser = new User("John", "Doe", new Address("First street", "12", "Brussels"));
        User expectedUser = new User("John", null, new Address("First street", null, null));
        want.object(actualUser).eqReflect(expectedUser, EqMode.IGNORE_DEFAULTS);
    }

    @Test
    public void test6() {
        Date actualDate = new Date(44444);
        Date expectedDate = new Date();
        want.object(actualDate).eqReflect(expectedDate, EqMode.IGNORE_DATES);

    }

    @Test
    public void testLenientAssert() {
        List<Integer> myList = Arrays.asList(3, 2, 1);
        want.collection(myList).eqIgnoreOrder(Arrays.asList(1, 2, 3));

        want.object("any").eqIgnoreDefault(null);
    }

    @Test
    public void testLenientAssert2() {
        want.exception(() ->
                        want.object(null).eqIgnoreDefault("any")
                , AssertionError.class);
    }

    @Test
    public void test7() {
        User user1 = new User(1, "John", "Doe");
        User user2 = new User("John", "Doe", new Address("First street", "", ""));

        want.object(user1).eqByProperties("id", 1);
        want.object(user2).eqByProperties("address.street", "First street");

        want.object(new User[]{new User("Jane"), new User("John")}).eqReflect(
                Arrays.asList(new User("John"), new User("Jane")), EqMode.IGNORE_ORDER);

        want.object(Arrays.asList(new User("John"), new User("Jane"))).eqReflect(
                new User[]{new User("Jane"), new User("John")}, EqMode.IGNORE_ORDER);

        want.array(new User[]{new User("Jane"), new User("John")}).eqReflect(
                Arrays.asList(new User("John"), new User("Jane")), EqMode.IGNORE_ORDER);

        want.collection(Arrays.asList(new User("John"), new User("Jane"))).eqReflect(
                new User[]{new User("Jane"), new User("John")}, EqMode.IGNORE_ORDER);

    }

    @Test
    public void test8() {
        User user = new User(1, "John", "Doe");
        want.exception(() ->
                        want.object(user).eqByProperties("id", 2)
                , AssertionError.class);
    }

    @Test
    public void test9() {
        User user = new User("John", "Doe", new Address("First street", "", ""));
        want.exception(() ->
                        want.object(user).eqByProperties("address.street", "First street1")
                , AssertionError.class);
    }

    @Test
    public void test10() {
        want.array(new User[]{new User("Jane", "Doe"), new User("John", "Doe")}).eqByProperties("first",
                Arrays.asList("Jane", "John"));
    }
}
