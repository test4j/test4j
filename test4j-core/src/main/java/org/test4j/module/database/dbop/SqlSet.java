package org.test4j.module.database.dbop;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.test4j.module.database.environment.DBEnvironment;
import org.test4j.module.database.utility.SqlRunner;
import org.test4j.tools.commons.ExceptionWrapper;

public class SqlSet implements ISqlSet {
    private List<String> list = new ArrayList<String>();

    public void sql(String sql) {
        this.list.add(sql);
    }

    public void readFrom(DBEnvironment env, String filename) {
        try {
            SqlRunner.executeFromFile(env, filename);
        } catch (Exception e) {
            throw ExceptionWrapper.wrapWithRuntimeException(e);
        }
    }

    public void readFrom(DBEnvironment env, File file) {
        try {
            SqlRunner.executeFromStream(env, new FileInputStream(file));
        } catch (Exception e) {
            throw ExceptionWrapper.wrapWithRuntimeException(e);
        }
    }

    public void readFrom(DBEnvironment env, InputStream stream) {
        try {
            SqlRunner.executeFromStream(env, stream);
        } catch (Exception e) {
            throw ExceptionWrapper.wrapWithRuntimeException(e);
        }
    }

    /**
     * 执行列表中的sql语句<br>
     * 执行完毕，列表不做清空，方便重用
     */
    public void execute(DBEnvironment env) {
        for (String sql : list) {
            SqlRunner.execute(env, sql);
        }
    }
}
