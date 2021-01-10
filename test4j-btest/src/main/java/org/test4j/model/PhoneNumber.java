package org.test4j.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wudarui
 */
@Data
@Accessors(chain = true)
public class PhoneNumber implements java.io.Serializable {
    private static final long serialVersionUID = 5646650408028947175L;
    private int code;
    private String number;

    public PhoneNumber(int code, String number) {
        this.code = code;
        this.number = number;
    }
}