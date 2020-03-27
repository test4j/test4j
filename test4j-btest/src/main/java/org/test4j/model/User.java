package org.test4j.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wudarui
 */
@Getter
@Setter
@Accessors(chain = true)
public class User implements java.io.Serializable {
    private static final long serialVersionUID = 1145595282412714496L;

    private long id;

    private String name;

    private String first;

    private String last;

    private int age;

    private String postcode;

    private double salary;

    private boolean isFemale = false;

    private Address address;

    private List<Address> addresses;

    private String[] phones;

    private User assistor;

    public User() {
    }

    public User(long id) {
        this.id = id;
    }

    public User(String first) {
        this.first = first;
    }

    public User(String first, String last) {
        this.first = first;
        this.last = last;
    }

    public User(long id, String first, String last) {
        this.id = id;
        this.first = first;
        this.last = last;
    }

    public User(long id, double salary) {
        this.id = id;
        this.salary = salary;
    }

    public User(String first, String last, Address address) {
        this.first = first;
        this.last = last;
        this.address = address;
    }


    public static User newUser(String name, String[] phones) {
        User user = new User();
        user.first = name;
        user.setPhones(phones);
        return user;
    }

    /**
     * 构造一个供测试的用的user对象
     *
     * @param name
     * @return
     */
    public static User mock(long id, String name) {
        return new User()
                .setId(id)
                .setName(name);
    }

    public static User mock() {
        User user = new User(1, "wu", "darui");
        user.setAddresses(new ArrayList<Address>() {
            private static final long serialVersionUID = 516532764093459888L;

            {
                this.add(new Address(2, "stree2"));
                this.add(new Address(3, "stree3"));
            }
        });
        return user;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + "]";
    }
}
