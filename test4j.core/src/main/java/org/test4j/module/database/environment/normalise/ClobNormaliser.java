package org.test4j.module.database.environment.normalise;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Clob;

public class ClobNormaliser implements TypeNormaliser {
    @Override
    public Object normalise(Object o) throws Exception {
        return clob2str((Clob) o);
    }

    private String clob2str(Clob clob) {
        try (Reader is = clob.getCharacterStream(); BufferedReader buff = new BufferedReader(is)) {
            String line = buff.readLine();
            StringBuffer sb = new StringBuffer();
            while (line != null) {
                sb.append(line);
                line = buff.readLine();
            }
            return sb.toString();
        } catch (Exception e) {
            String err = "java.sql.Clob 类型转 String出错:" + e.getMessage();
            throw new RuntimeException(err, e);
        }
    }
}
