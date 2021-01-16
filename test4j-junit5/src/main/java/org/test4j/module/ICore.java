package org.test4j.module;

import java.util.Map;

/**
 * use {@link org.test4j.tools.datagen.DataMap}
 *
 * @author wudarui
 */
@Deprecated
public class ICore {
    public static class DataMap<T> extends org.test4j.tools.datagen.DataMap<T> {
        public DataMap() {
        }

        public DataMap(int rowSize) {
            super(rowSize);
        }

        public DataMap(Map<String, Object> map) {
            super(map);
        }
    }
}