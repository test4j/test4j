package org.test4j.tools.datagen;

import org.test4j.tools.commons.ArrayHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataProvider<T> implements Iterator<T[]> {
    private List<T[]> datas = new ArrayList<T[]>();
    private Iterator<T[]> it = null;

    public DataProvider data(T... data) {
        this.checkDataLength(data);
        this.datas.add(data);
        this.index++;
        return this;
    }

    public boolean hasNext() {
        this.initIterator();
        return it.hasNext();
    }

    public T[] next() {
        this.initIterator();
        return it.next();
    }

    public void remove() {
        this.initIterator();
        it.remove();
    }

    private Lock lock = new ReentrantLock();

    private void initIterator() {
        if (it == null) {
            try {
                lock.lock();
                if (it == null) {
                    it = this.datas.iterator();
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private int index = 1;
    private int prev = 0;

    private String ERROR_MSG = "DataProvider error, the previous data length is %d, but current data(data index %d) %s length is %d." ;

    /**
     * 检查数据长度是否一致
     *
     * @param data
     */
    private void checkDataLength(T... data) {
        int length = data.length;
        if (length == 0) {
            throw new RuntimeException(String.format("provider data(index %d) error, can't be empty.", index));
        }
        if (prev == 0 || prev == length) {
            prev = length;
        } else {
            String datas = ArrayHelper.toString(data);
            String error = String.format(ERROR_MSG, prev, index, datas, length);
            throw new RuntimeException(error);
        }
    }
}
