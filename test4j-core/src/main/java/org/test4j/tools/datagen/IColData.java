package org.test4j.tools.datagen;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.test4j.tools.json.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * IColData 列对象
 *
 * @author wudarui
 */
public interface IColData {
    /**
     * 返回第 row 行列对象
     *
     * @param row
     * @return
     */
    Object row(int row);

    /**
     * 返回 rows 行列对象
     *
     * @param rows 行数
     * @return
     */
    List rows(int rows);

    /**
     * 返回默认行列对象
     *
     * @return
     */
    List rows();

    /**
     * 设置普通值或增加列对象
     *
     * @param value
     * @param <T>
     * @return
     */
    <T extends IColData> T add(Object value);


    /**
     * 单行列对象
     */
    @Getter
    @Setter
    @EqualsAndHashCode
    class OneRowValue implements IColData {
        private Object value;

        public OneRowValue(Object value) {
            this.value = value;
        }

        @Override
        public OneRowValue add(Object value) {
            this.value = value;
            return this;
        }

        @Override
        public Object row(int row) {
            return value;
        }

        @Override
        public List rows(int size) {
            List list = new ArrayList(size);
            for (int index = 0; index < size; index++) {
                list.add(this.value);
            }
            return list;
        }

        @Override
        public List rows() {
            return this.rows(1);
        }

        @Override
        public String toString() {
            return JSON.toJSON(this.value, false);
        }
    }

    /**
     * 多行列对象
     */
    @Getter
    @Setter
    @EqualsAndHashCode
    class MulRowValue implements IColData {
        private final List list = new ArrayList();

        private AbstractDataGenerator generator = null;

        @Override
        public MulRowValue add(Object value) {
            if (value instanceof AbstractDataGenerator) {
                generator = (AbstractDataGenerator) value;
            } else {
                this.list.add(value);
            }
            return this;
        }

        @Override
        public Object row(int row) {
            if (row < 0) {
                throw new RuntimeException("index can't less than zero.");
            }
            int size = list.size();
            if (row < size) {
                return list.get(row);
            } else if (generator == null) {
                return list.isEmpty() ? null : list.get(size - 1);
            } else {
                return generator.generate(row);
            }
        }

        @Override
        public List rows(int size) {
            List list = new ArrayList(size);
            for (int index = 0; index < size; index++) {
                list.add(this.row(index));
            }
            return list;
        }

        @Override
        public List rows() {
            return this.rows(this.list.isEmpty() ? 1 : this.list.size());
        }

        @Override
        public String toString() {
            return JSON.toJSON(this.rows(), false);
        }
    }
}
