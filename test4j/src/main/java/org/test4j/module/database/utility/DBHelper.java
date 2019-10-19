package org.test4j.module.database.utility;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.BiConsumer;

import org.test4j.module.ICore.DataMap;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.module.database.environment.normalise.TypeNormaliser;
import org.test4j.module.database.environment.normalise.TypeNormaliserFactory;
import org.test4j.tools.commons.ClazzHelper;
import org.test4j.tools.commons.StringHelper;
import org.test4j.exception.NoSuchFieldRuntimeException;
import org.test4j.tools.datagen.IDataMap;
import org.test4j.tools.reflector.FieldAccessor;

import static org.test4j.tools.commons.StringHelper.join;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class DBHelper {

    /**
     * 关闭数据库statement句柄
     */
    public static void close(AutoCloseable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
            closeable = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 将ResultSet的当前行转换为Map数据返回
     *
     * @param rs 数据库结果ResultSet
     * @return
     * @throws SQLException
     */
    public static Map getMapFromResult(ResultSet rs, boolean isCamelName) throws Exception {
        ResultSetMetaData rsmd = rs.getMetaData();
        if (rs.next()) {
            return buildMap(rs, isCamelName, rsmd);
        } else {
            return null;
        }
    }

    /**
     * 将ResultSet的转换为Map List数据返回
     *
     * @param rs
     * @param isCamelName
     * @return
     * @throws SQLException
     */
    public static List<Map> getListMapFromResult(ResultSet rs, boolean isCamelName)
            throws Exception {
        List<Map> list = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        while (rs.next()) {
            Map map = buildMap(rs, isCamelName, rsmd);
            list.add(map);
        }

        return list;
    }


    /**
     * 将ResultSet的当前行转换为PoJo数据返回
     *
     * @param <T>
     * @param rs
     * @param clazz
     * @return
     * @throws SQLException
     */
    public static <T> T getPoJoFromResult(ResultSet rs, Class<T> clazz) throws Exception {
        if (rs.next()) {
            ResultSetMetaData rsmd = rs.getMetaData();
            return buildPoJo(rs, clazz, rsmd);
        } else {
            return null;
        }
    }

    private static <T> T buildPoJo(ResultSet rs, Class<T> clazz, ResultSetMetaData rsmd) throws SQLException {
        int count = rsmd.getColumnCount();
        T pojo = count < 2 ? null : ClazzHelper.newInstance(clazz);
        Object firstField = buildObject(rs, true, rsmd, (key, value) -> {
            if (pojo != null) {
                new FieldAccessor(clazz, key).set(pojo, value);
            }
        });
        return pojo == null ? (T) firstField : pojo;
    }


    private static Map buildMap(ResultSet rs, boolean isCamelName, ResultSetMetaData rsmd) {
        Map<String, Object> map = new HashMap<>();
        buildObject(rs, isCamelName, rsmd, (key, value) -> {
            map.put(key, value);
        });
        return map;
    }

    private static Object buildObject(ResultSet rs, boolean isCamelName, ResultSetMetaData rsmd, BiConsumer<String, Object> consumer) {
        try {
            int count = rsmd.getColumnCount();
            Object first = null;
            for (int index = 1; index <= count; index++) {
                String key = getCamelFieldName(rsmd, index, isCamelName);
                String columnClaz = rsmd.getColumnClassName(index);
                Object o = null;
                if (java.sql.Timestamp.class.getName().endsWith(columnClaz)) {
                    o = rs.getTimestamp(index);
                } else if (java.sql.Clob.class.getName().endsWith(columnClaz)) {
                    o = rs.getClob(index);
                } else {
                    o = rs.getObject(index);
                }
                Object value = DBHelper.normaliseValue(columnClaz, o);
                consumer.accept(key, value);
                if (index == 1) {
                    first = value;
                }
            }
            return first;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将ResultSet的当前行转换为PoJo列表数据返回
     *
     * @param <T>
     * @param rs
     * @param clazz
     * @return
     * @throws SQLException
     */
    public static <T> List<T> getListPoJoFromResult(ResultSet rs, Class<T> clazz)
            throws Exception {
        List list = new ArrayList();
        while (rs.next()) {
            T o = getPoJoFromResult(rs, clazz);
            list.add(o);
        }
        return list;
    }

    /**
     * 返回数据库查询结果集第index个字段的camel名称
     *
     * @param rsmd
     * @param index
     * @param isCamelName 是否过滤非法字符后返回驼峰命名<br>
     *                    false:返回数据库的原生字段名称
     * @return
     * @throws SQLException
     */
    private static String getCamelFieldName(ResultSetMetaData rsmd, int index, boolean isCamelName) throws SQLException {
        String columnName = rsmd.getColumnName(index);
        if (isCamelName) {
            columnName = columnName.replaceAll("[^a-zA-Z0-9]", " ");
            String fieldName = StringHelper.camel(columnName.toLowerCase());
            return fieldName;
        } else {
            return columnName;
        }
    }

    /**
     * 填充数据
     *
     * @param map
     * @param table
     */
    public static void fillData(Map<String, Object> map, String table) {

    }

    /**
     * 分解sql语句为单条可执行的sql语句集合,并过滤注释
     *
     * @param content 多条sql语句(可能包含注释，换行等信息)
     * @return
     */
    public static String[] parseSQL(String content) {
        char[] chars = content.toCharArray();
        List<String> statements = new ArrayList<String>();

        StamentStatus status = StamentStatus.NORMAL;
        StringBuffer buff = new StringBuffer();
        for (int index = 0; index < chars.length; index++) {
            char ch = chars[index];
            char next = '\0';
            switch (status) {
                case SINGLE_NOTE:
                    if (ch == '\n' || ch == '\r') {
                        buff.append(' ');
                        status = StamentStatus.NORMAL;
                    }
                    break;
                case MULTI_NOTE:
                    next = (index == chars.length - 1) ? '/' : chars[index + 1];
                    if (ch == '*' && next == '/') {
                        index++;
                        status = StamentStatus.NORMAL;
                    }
                    break;
                case SINGLE_QUOTATION:
                    buff.append(ch);
                    if (ch == '\'') {
                        status = StamentStatus.NORMAL;
                    }
                    break;
                case DOUBLE_QUOTATION:
                    buff.append(ch);
                    if (ch == '"') {
                        status = StamentStatus.NORMAL;
                    }
                    break;
                case NORMAL:
                    next = (index == chars.length - 1) ? ';' : chars[index + 1];
                    if (ch == '-' && next == '-') {
                        index++;
                        status = StamentStatus.SINGLE_NOTE;
                    } else if (ch == '/' && next == '*') {
                        index++;
                        status = StamentStatus.MULTI_NOTE;
                    } else if (ch == '\'') {
                        buff.append(ch);
                        status = StamentStatus.SINGLE_QUOTATION;
                    } else if (ch == '"') {
                        buff.append(ch);
                        status = StamentStatus.DOUBLE_QUOTATION;
                    } else if (ch == ';') {
                        String statement = buff.toString().trim();
                        if ("".equals(statement) == false) {
                            statements.add(statement);
                        }
                        buff = new StringBuffer();
                    } else if (ch == '\n' || ch == '\r') {
                        buff.append(' ');
                    } else {
                        buff.append(ch);
                    }
                    break;
            }
        }
        String statement = buff.toString().trim();
        if ("".equals(statement) == false) {
            statements.add(statement);
        }

        String[] stmts = new String[statements.size()];
        statements.toArray(stmts);
        return stmts;
    }

    /**
     * 根据DataMap构造查询条件
     *
     * @param map
     * @return
     */
    public static String getWhereCondiction(IDataMap map) {
        if (map == null || map.getValueSize() == 0) {
            return "";
        }
        StringBuilder where = new StringBuilder();
        where.append(" where ");
        boolean isFirst = true;
        for (String key : (Set<String>) map.keySet()) {
            if (isFirst) {
                isFirst = false;
            } else {
                where.append(" and ");
            }
            where.append(key).append("=?");
        }
        return where.toString();
    }

    public static Object normaliseValue(String klass, Object currVal) throws Exception {
        if (currVal == null) {
            return null;
        }
        TypeNormaliser tn = TypeNormaliserFactory.getNormaliser(klass);
        return tn == null ? currVal : tn.normalise(currVal);
    }

    /**
     * 构建h2数据库表table的唯一键脚本
     *
     * @param table  指定表
     * @param fields 索引字段
     * @return
     */
    public static String buildH2Unique(String table, String... fields) {
        if (fields == null || fields.length == 0) {
            return "";
        }
        return new StringBuilder()
                .append(join(String.format("ALTER TABLE %s ADD CONSTRAINT UNIQ_", table), "_", fields, " "))
                .append(join("UNIQUE(", ",", fields, ");"))
                .toString().toUpperCase();
    }

    /**
     * 构建h2数据库表table的普通索引脚本
     *
     * @param table  指定表
     * @param fields 索引字段
     * @return
     */
    public static String buildH2Index(String table, String... fields) {
        if (fields == null || fields.length == 0) {
            return "";
        }
        return new StringBuilder()
                .append(join(String.format("CREATE INDEX IDX_%s_", table), "_", fields, " "))
                .append(join(table + "ON (", ",", fields, "); "))
                .toString().toUpperCase();
    }
}
