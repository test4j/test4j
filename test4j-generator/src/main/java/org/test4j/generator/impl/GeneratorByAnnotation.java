package org.test4j.generator.impl;

import org.apache.ibatis.type.UnknownTypeHandler;
import org.test4j.generator.annotation.Column;
import org.test4j.generator.annotation.Table;
import org.test4j.generator.annotation.Tables;
import org.test4j.generator.config.IGlobalConfig;
import org.test4j.generator.config.IGlobalConfigSet;
import org.test4j.generator.config.ITableSetter;
import org.test4j.generator.config.constant.DefinedColumn;
import org.test4j.generator.db.IFieldCategory;

import java.util.Objects;
import java.util.function.Consumer;

import static org.test4j.generator.convert.Util.NOT_DEFINED;
import static org.test4j.generator.convert.Util.isBlank;

/**
 * 根据注解生成Entity文件
 *
 * @author wudarui
 */
public class GeneratorByAnnotation {

    public static void generate(Class clazz) {
        Tables tables = (Tables) clazz.getAnnotation(Tables.class);
        if (tables.tables().length == 0) {
            throw new RuntimeException("the @Tables Annotation not found.");
        }
        GeneratorByAnnotation generator = new GeneratorByAnnotation(tables);
        IGlobalConfig globalConfig = generator.globalConfig();
        globalConfig
            .globalConfig(generator.getGlobalConfig(tables))
            .tables(tc -> {
                for (Table table : tables.tables()) {
                    for (String tableName : table.value()) {
                        Consumer<ITableSetter> consumer = generator.getTableConfig(table);
                        tc.table(tableName, consumer);
                    }
                }
            })
            .execute();
    }

    private Consumer<IGlobalConfigSet> getGlobalConfig(Tables tables) {
        return g -> {
            g.setDataSource(tables.url(), tables.username(), tables.password());
            g.setOutputDir(this.srcDir, this.testDir, this.daoDir);
            g.setBasePackage(tables.basePack());
            g.setDaoPackage(tables.basePack());
        };
    }

    private Consumer<ITableSetter> getTableConfig(Table table) {
        return t -> {
            if (table.excludes().length > 0) {
                t.setExcludes(table.excludes());
            }
            t.setGmtCreate(value(table.gmtCreated(), tables.gmtCreated()));
            t.setGmtModified(value(table.gmtModified(), tables.gmtModified()));
            t.setLogicDeleted(value(table.logicDeleted(), tables.logicDeleted()));
            t.setSeqName(table.seqName());
            t.setTablePrefix(value(table.tablePrefix(), tables.tablePrefix()));
            t.setMapperPrefix(value(table.mapperPrefix(), tables.mapperPrefix()));
            for (Class dao : table.dao()) {
                t.addBaseDaoInterface(dao);
            }
            for (Class entity : table.entity()) {
                t.addEntityInterface(entity);
            }
            for (Column column : table.columns()) {
                t.setColumn(column.value(), getDefinedColumnConsumer(t, column));
            }
        };
    }

    /**
     * 显式定义字段处理
     *
     * @param ts     表定义全局配置
     * @param column
     * @return
     */
    private Consumer<DefinedColumn> getDefinedColumnConsumer(ITableSetter ts, Column column) {
        return c -> {
            /** 先处理category, 保证个性化设置的覆盖 **/
            if (column.category() == IFieldCategory.GmtCreate) {
                ts.setGmtCreate(column.value());
            } else if (column.category() == IFieldCategory.GmtModified) {
                ts.setGmtModified(column.value());
            } else if (column.category() == IFieldCategory.IsDeleted) {
                ts.setLogicDeleted(column.value());
            }
            /** 个性化设置 **/
            if (!isBlank(column.property())) {
                c.setFieldName(column.property());
            }
            if (!isBlank(column.insert())) {
                c.setInsert(column.insert());
            }
            if (!isBlank(column.update())) {
                c.setUpdate(column.update());
            }
            if (column.isLarge()) {
                c.setLarge();
            }
            if (!Objects.equals(column.javaType(), Object.class)) {
                c.setJavaType(column.javaType());
            }
            if (!Objects.equals(column.typeHandler(), UnknownTypeHandler.class)) {
                c.setTypeHandler(column.typeHandler());
            }
        };
    }

    private IGlobalConfig globalConfig() {
        return GeneratorByApi.build(!isBlank(tables.srcDir()), !isBlank(tables.testDir()));
    }

    private final Tables tables;

    private final String srcDir;

    private final String testDir;

    private final String daoDir;

    private GeneratorByAnnotation(Tables tables) {
        this.tables = tables;
        this.srcDir = System.getProperty("user.dir") + "/" + tables.srcDir() + "/";
        this.testDir = System.getProperty("user.dir") + "/" + tables.testDir() + "/";
        this.daoDir = System.getProperty("user.dir") + "/" + tables.daoDir() + "/";
    }


    private String value(String value1, String value2) {
        String value = !NOT_DEFINED.equals(value1) ? value1 : NOT_DEFINED.equals(value2) ? "" : value2;
        return value;
    }

    private String[] value(String[] value1, String[] value2) {
        String[] value = isDefined(value1) ? value1 : isDefined(value2) ? value2 : new String[0];
        return value;
    }

    private boolean isDefined(String[] value) {
        return value.length != 1 || !Objects.equals(value[0], NOT_DEFINED);
    }

}