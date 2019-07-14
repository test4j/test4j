package org.test4j.module;

import org.test4j.hamcrest.TheStyleAssertion;
import org.test4j.hamcrest.WantStyleAssertion;
import org.test4j.tools.commons.ArrayHelper;
import org.test4j.tools.commons.Reflector;
import org.test4j.tools.datagen.AbstractDataGenerator;
import org.test4j.tools.datagen.AbstractDataMap;
import org.test4j.tools.datagen.DataProviderIterator;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public interface ICore {
    WantStyleAssertion want = new WantStyleAssertion();

    TheStyleAssertion the = new TheStyleAssertion();

    Reflector reflector = Reflector.instance;

    /**
     * 构造一个DataMap
     *
     * @return
     */
    default DataMap map() {
        return DataMap.create();
    }

    /**
     * 构造一个DataMap
     *
     * @param size
     * @return
     */
    default DataMap map(int size) {
        return DataMap.create(size);
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

    class DataMap<DM extends DataMap> extends AbstractDataMap<DM> {
        public DataMap() {
            super();
        }

        public DataMap(int dataSize) {
            super(dataSize);
        }

        public static DataMap create(int dataSize) {
            return new DataMap<>(dataSize);
        }

        public static DataMap create() {
            return new DataMap();
        }
    }

    class DataIterator extends DataProviderIterator<Object> {
    }
}
