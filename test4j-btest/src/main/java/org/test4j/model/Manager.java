package org.test4j.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wudarui
 */
@Data
@Accessors(chain = true)
public class Manager extends Employee {
    private static final long serialVersionUID = 843725563822394654L;
    private Employee secretary;

    private Serializable phoneNumber;

    public Manager() {
        super();
    }

    public Manager(String name, double sarary) {
        super(name, sarary);
    }

    public static Manager mock() {
        return (Manager) new Manager("Tony Tester", 80000)
                .setSecretary(new Employee("Harry Hacker", 50000))
                .setPhoneNumber(new PhoneNumber(571, "0571-88886666"))
                .setDate(new Date());
    }
}
