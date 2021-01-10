package org.test4j.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.*;

/**
 * @author wudarui
 */
@Data
@Accessors(chain = true)
public class ComplexObject {
    private String name;

    private double sarary;

    private Date date;

    private Serializable phoneNumber;

    private User user;

    private Collection<String> addresses;

    private Map<String, Serializable> map;

    public static ComplexObject instance() {
        return new ComplexObject()
                .setName("I am a test")
                .setDate(new Date())
                .setPhoneNumber(new PhoneNumber("0571", "88886666"))
                .setSarary(2000d)
                .setUser(new User("wu", "davey"))
                .setAddresses(Arrays.asList("地址一", "地址二", "地址三"))
                .setMap(new HashMap<String, Serializable>() {
                    private static final long serialVersionUID = -6999560873523992661L;

                    {
                        this.put("key1", "value1");
                        this.put("key2", new User("darui.wu"));
                    }
                });
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("name=" + this.name);
        buffer.append("\n");
        buffer.append("sarary=" + this.sarary);
        buffer.append("\n");
        buffer.append("addresses=" + this.addresses.toString());
        return buffer.toString();
    }

    @Data
    @Accessors(chain = true)
    public static class PhoneNumber implements Serializable {
        private static final long serialVersionUID = 8567955906309667163L;

        private String areaNo;

        private String phone;

        public PhoneNumber(String areaNo, String phone) {
            this.areaNo = areaNo;
            this.phone = phone;
        }
    }
}