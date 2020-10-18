package org.test4j.generator.javafile;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import org.test4j.generator.config.impl.TableSetter;

public class DaoInterfaceFile extends BaseFile {

    static ClassName IBaseDao = ClassName.get("cn.org.atool.fluent.mybatis.base", "IBaseDao");

    public DaoInterfaceFile(TableSetter table) {
        super(table);
        this.packageName = daoPackage(table);
        this.klassName = daoClass(table);
    }

    public static ClassName daoInterfaceName(TableSetter table) {
        return ClassName.get(daoPackage(table), daoClass(table));
    }

    public static String daoPackage(TableSetter table) {
        return table.getBasePackage() + ".dao.intf";
    }

    public static String daoClass(TableSetter table) {
        return table.getEntityPrefix() + "Dao";
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.addSuperinterface(parameterizedType(IBaseDao, EntityFile.entityName(table)));
        builder.addJavadoc("$T: 数据操作接口", EntityFile.entityName(table));
        builder.addJavadoc("@author Powered By Fluent Mybatis");
    }

    @Override
    protected boolean isInterface() {
        return true;
    }
}
