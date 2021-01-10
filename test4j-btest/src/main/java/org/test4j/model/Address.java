package org.test4j.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wudarui
 */
@Data
@Accessors(chain = true)
public class Address implements java.io.Serializable {
    private static final long serialVersionUID = -8560639654630006910L;

    private long id;

    private String street;

    private String postcode;

    private String name;

    public Address() {
    }

    public Address(String street) {
        this.street = street;
    }

    public Address(long id, String street) {
        this.id = id;
        this.street = street;
    }

    public Address(String street, String postcode, String name) {
        this.street = street;
        this.postcode = postcode;
        this.name = name;
    }
}