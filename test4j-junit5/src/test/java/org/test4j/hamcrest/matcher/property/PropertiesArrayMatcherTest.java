package org.test4j.hamcrest.matcher.property;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;
import org.test4j.model.User;

public class PropertiesArrayMatcherTest extends Test4J {

    @Test
    public void testMatches() {
        want.object(new User("dfasdf", "eedaf")).propertyEq(new String[]{"first", "last"},
                new Object[]{"dfasdf", "eedaf"});
    }

    @Test
    public void testMatches_Array() {
        List<User> users = Arrays.asList(new User("dfa123sdf", "abc"), new User("firs123tname", "abc"));
        want.collection(users).propertyMatch("first", the.collection().hasAllItems("dfa123sdf", "firs123tname"))
                .propertyEq("last", new String[]{"abc", "abc"});
    }

    @Test
    public void testMatches_Map() {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("first", "aaaaa 123 ddd");
        maps.put("last", "abc");
        want.map(maps).propertyMatch("first", the.string().contains("123")).propertyEq("last", "abc");
    }
}
