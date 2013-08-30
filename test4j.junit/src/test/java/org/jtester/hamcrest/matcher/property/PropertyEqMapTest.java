package org.jtester.hamcrest.matcher.property;

import org.jtester.fortest.beans.User;
import org.jtester.junit.JTester;
import org.junit.Test;

@SuppressWarnings("serial")
public class PropertyEqMapTest implements JTester {
    @Test(expected = AssertionError.class)
    public void testPropertyEqMap() {
        User user = new User();
        user.setAssistor(User.newUser("siri", new String[] { "139xxx", "159xxx" }));
        want.object(user).propertyEqMap(new DataMap() {
            {
                this.put("assistor.phones", new String[] { "133xxx", "131xxx" });
            }
        });
    }

    @Test(expected = AssertionError.class)
    public void testPropertyEqMap_List() {
        User[] users = new User[] { new User() {
            {
                setAssistor(User.newUser("siri", new String[] { "139xxx", "159xxx" }));
            }
        }, new User() {
            {
                setAssistor(User.newUser("wade", new String[] { "130xxx", "0571xx" }));
            }
        } };
        want.list(users).propertyEqMap(2, new DataMap() {
            {
                this.put("assistor.phones",// <br>
                        new String[] { "133xxx", "131xxx" },// <br>
                        new String[] { "130xxx", "0571xx" });
            }
        });
    }

    @Test
    public void testPropertyEqMap_List_Equals() {
        User[] users = new User[] { new User() {
            {
                setAssistor(User.newUser("siri", new String[] { "139xxx", "159xxx" }));
            }
        }, new User() {
            {
                setAssistor(User.newUser("wade", new String[] { "130xxx", "0571xx" }));
            }
        } };
        want.list(users).propertyEqMap(2, new DataMap() {
            {
                this.put("assistor.phones",// <br>
                        new String[] { "139xxx", "159xxx" },// <br>
                        new String[] { "130xxx", "0571xx" });
            }
        });
    }
}
