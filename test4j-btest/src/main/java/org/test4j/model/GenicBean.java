package org.test4j.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GenicBean<T> {
    String name;

    T refObject;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static GenicBean newInstance(String name, Object ref) {
        GenicBean bean = new GenicBean();
        bean.name = name;
        bean.refObject = ref;
        return bean;
    }
}
