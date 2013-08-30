package org.jtester.tools.datagen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jtester.tools.commons.ArrayHelper;

public class DataProviderIterator<T> implements Iterator<T[]> {
    private List<T[]>     datas = new ArrayList<T[]>();
    private Iterator<T[]> it    = null;

    public void data(T... data) {
        this.checkDataLength(data);
        this.datas.add(data);
        this.index++;
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

    private synchronized void initIterator() {
        if (it == null) {
            it = this.datas.iterator();
        }
    }

    private int    index     = 1;
    private int    prev      = 0;

    private String ERROR_MSG = "DataProvider error, the previous data length is %d, but current data(data index %d) %s length is %d.";

    /**
     * 检查数据长度是否一致
     * 
     * @param o
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
