package org.jtester.module.database.dbop;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jtester.module.database.utility.SqlRunner;
import org.jtester.tools.commons.ExceptionWrapper;

public class SqlSet implements ISqlSet {
    private List<String> list = new ArrayList<String>();

    public void sql(String sql) {
        this.list.add(sql);
    }

    public void readFrom(String filename) {
        try {
            SqlRunner.instance.executeFromFile(filename);
        } catch (Exception e) {
            throw ExceptionWrapper.wrapWithRuntimeException(e);
        }
    }

    public void readFrom(File file) {
        try {
            SqlRunner.instance.executeFromStream(new FileInputStream(file));
        } catch (Exception e) {
            throw ExceptionWrapper.wrapWithRuntimeException(e);
        }
    }

    public void readFrom(InputStream stream) {
        try {
            SqlRunner.instance.executeFromStream(stream);
        } catch (Exception e) {
            throw ExceptionWrapper.wrapWithRuntimeException(e);
        }
    }

    /**
     * 执行列表中的sql语句<br>
     * 执行完毕，列表不做清空，方便重用
     */
    public void execute() {
        for (String sql : list) {
            SqlRunner.instance.execute(sql);
        }
    }
}
