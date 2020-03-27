package org.test4j.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author wudarui
 */
@Data
@Accessors(chain = true)
public class Employee implements java.io.Serializable {
    private static final long serialVersionUID = -7583085914565894622L;

    private String name;

    private transient double sarary;

    private Date date;

    public Employee() {
        super();
    }

    public Employee(String name, double sarary) {
        this.name = name;
        this.sarary = sarary;
    }
}
