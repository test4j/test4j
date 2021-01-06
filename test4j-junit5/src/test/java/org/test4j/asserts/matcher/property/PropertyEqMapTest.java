package org.test4j.asserts.matcher.property;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;
import org.test4j.model.User;

@SuppressWarnings("serial")
public class PropertyEqMapTest extends Test4J {
    @Test
    public void testPropertyEqMap() {
        User user = new User();
        user.setAssistor(User.newUser("siri", new String[]{"139xxx", "159xxx"}));
        want.exception(() ->
                        want.object(user).eqDataMap(new DataMap() {
                            {
                                this.kv("assistor.phones", new String[]{"133xxx", "131xxx"});
                            }
                        })
                , AssertionError.class);
    }

    @Test
    public void testPropertyEqMap_List() {
        User[] users = new User[]{new User() {
            {
                setAssistor(User.newUser("siri", new String[]{"139xxx", "159xxx"}));
            }
        }, new User() {
            {
                setAssistor(User.newUser("wade", new String[]{"130xxx", "0571xx"}));
            }
        }};
        want.exception(() ->
                        want.list(users).eqReflect(new DataMap(2) {
                            {
                                this.kv("assistor.phones",// <br>
                                        (Object) new String[]{"133xxx", "131xxx"},// <br>
                                        (Object) new String[]{"130xxx", "0571xx"});
                            }
                        })
                , AssertionError.class);
    }

    @Test
    public void testPropertyEqMap_List_Equals() {
        User[] users = new User[]{new User() {
            {
                setAssistor(User.newUser("siri", new String[]{"139xxx", "159xxx"}));
            }
        }, new User() {
            {
                setAssistor(User.newUser("wade", new String[]{"130xxx", "0571xx"}));
            }
        }};
        want.list(users).eqDataMap(new DataMap(2) {
            {
                this.kv("assistor.phones",// <br>
                        (Object) new String[]{"139xxx", "159xxx"},// <br>
                        (Object) new String[]{"130xxx", "0571xx"});
            }
        });
    }
}
