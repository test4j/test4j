package org.test4j.module;

import org.test4j.asserts.IAssertion;
import org.test4j.tools.commons.ArrayHelper;
import org.test4j.tools.commons.Reflector;
import org.test4j.tools.datagen.AbstractDataGenerator;
import org.test4j.tools.datagen.AbstractDataMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public interface ICore extends IAssertion {
    Reflector reflector = Reflector.instance;

    /**
     * 构造一个非行列式DataMap
     *
     * @return
     */
    default DataMap map() {
        return DataMap.create();
    }

    /**
     * 构造一个行列式DataMap
     *
     * @param colSize
     * @return
     */
    default DataMap map(int colSize) {
        return DataMap.create(colSize);
    }

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
     * 数据生成器<br>
     * index计数从0开始
     *
     * @author darui.wudr
     */
    abstract class DataGenerator extends AbstractDataGenerator {
    }

    /**
     * 行列式对象
     *
     * @param <DM>
     */
    class DataMap<DM extends DataMap> extends AbstractDataMap {
        /**
         * 创建一个普通的Map对象
         */
        public DataMap() {
            super();
        }

        public DataMap(Map<String, Object> map) {
            super();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                this.kv(entry.getKey(), entry.getValue());
            }
        }

        /**
         * 创建一个colSize行的行列式对象
         *
         * @param colSize
         */
        public DataMap(int colSize) {
            super(colSize);
        }

        /**
         * 创建一个普通的Map对象
         *
         * @return
         */
        public static DataMap create() {
            return new DataMap();
        }

        /**
         * 创建一个colSize行的行列式对象
         *
         * @param colSize
         * @return
         */
        public static DataMap create(int colSize) {
            return new DataMap<>(colSize);
        }

        /**
         * @param text
         * @return
         */
        public static DataMap create(String text) {

            return null;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
