package org.test4j;

import org.test4j.asserts.IWant;
import org.test4j.tools.IUtils;
import org.test4j.tools.commons.ArrayHelper;
import org.test4j.tools.datagen.AbstractDataGenerator;
import org.test4j.tools.datagen.AbstractDataMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface Test4J extends ISpring, IDatabase, IWant, IUtils {
    /**
     * 构造空list
     *
     * @return
     */
    default List arr() {
        return new ArrayList();
    }

    /***
     * 构造list对象
     *
     * @param value
     * @param values
     * @return
     */
    default List arr(Object value, Object... values) {
        return ArrayHelper.arr(value, values);
    }

    /**
     * 构造一个非行列式DataMap
     *
     * @return
     */
    default DataMap map() {
        return new DataMap();
    }

    /**
     * 构造一个行列式DataMap
     *
     * @param colSize
     * @return
     */
    default DataMap map(int colSize) {
        return new DataMap(colSize);
    }


    /**
     * 数据生成器<br>
     * index计数从0开始
     *
     * @author darui.wudr
     */
    abstract class DataGenerator extends AbstractDataGenerator {
    }

    class DataMap<DM extends DataMap> extends AbstractDataMap {
        public DataMap() {
            super();
        }

        public DataMap(Map<String, Object> map) {
            super(map);
        }

        public DataMap(int colSize) {
            super(colSize);
        }
    }
}