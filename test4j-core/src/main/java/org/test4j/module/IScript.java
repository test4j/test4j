package org.test4j.module;

import lombok.Data;
import lombok.experimental.Accessors;
import org.test4j.module.database.utility.EntityScriptParser;
import org.test4j.module.database.utility.EntityScriptParser.DbTypeConvert;
import org.test4j.module.database.utility.EntityScriptParser.NonDbTypeConvert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.test4j.module.database.utility.DBHelper.buildH2Index;
import static org.test4j.module.database.utility.DBHelper.buildH2Unique;

public interface IScript {
    /**
     * 指定的类型转换
     */
    Map<String, String> SPEC_TYPES = new HashMap<>();

    /**
     * 返回数据库表定义对象
     *
     * @return
     */
    Class[] getTableKlass();

    /**
     * 返回索引定义对象
     *
     * @return
     */
    IndexList getIndexList();

    /**
     * 生成数据库脚本
     *
     * @return
     */
    default String script() {
        return EntityScriptParser.script(this.dbTypeConvert(), this.getTableKlass()) +
                "\n\n" +
                IndexList.script(this.getIndexList());
    }

    /**
     * 构造测试库字段类型和原生库字段类型映射器
     *
     * @return
     */
    default DbTypeConvert dbTypeConvert() {
        if (SPEC_TYPES == null || SPEC_TYPES.size() == 0) {
            return new NonDbTypeConvert();
        } else {
            return type -> {
                String _type = type.toLowerCase();
                return SPEC_TYPES.containsKey(_type) ? SPEC_TYPES.get(_type) : _type;
            };
        }
    }

    @Data
    @Accessors(chain = true)
    class Index {
        private boolean isUnique;

        private String table;

        private String[] columns;

        public String buildIndex() {
            return isUnique ? buildH2Unique(this.table, columns) : buildH2Index(this.table, columns);
        }
    }

    class IndexList {
        List<Index> indexList = new ArrayList<>();

        public IndexList unique(String table, String... columns) {
            this.indexList.add(new Index().setUnique(true).setTable(table).setColumns(columns));
            return this;
        }

        public IndexList index(String table, String... columns) {
            this.indexList.add(new Index().setUnique(false).setTable(table).setColumns(columns));
            return this;
        }

        /**
         * 构建数据库索引
         *
         * @return
         */
        public static String script(IndexList indexList) {
            if (indexList == null || indexList.indexList == null || indexList.indexList.size() == 0) {
                return "";
            } else {
                return indexList.indexList.stream()
                        .map(Index::buildIndex)
                        .collect(Collectors.joining("\n"));
            }
        }
    }
}