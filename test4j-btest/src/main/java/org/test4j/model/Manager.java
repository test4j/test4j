package org.test4j.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wudarui
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Manager extends Employee {
    private static final long serialVersionUID = 843725563822394654L;
    private Employee secretary;

    private PhoneNumber phoneNumber;

    public Manager() {
        super();
    }

    public Manager(String name, double salary) {
        super(name, salary);
    }

    public static Manager mock() {
        return (Manager) new Manager("Tony Tester", 80000)
                .setSecretary(new Employee("Harry Hacker", 50000))
                .setPhoneNumber(new PhoneNumber(571, "0571-88886666"))
                .setDate(new Date());
    }
}