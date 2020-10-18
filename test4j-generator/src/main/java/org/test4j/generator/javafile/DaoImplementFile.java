package org.test4j.generator.javafile;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.test4j.generator.config.impl.TableSetter;

public class DaoImplementFile extends BaseFile {
    static ClassName Repository = ClassName.get("org.springframework.stereotype", "Repository");

    public DaoImplementFile(TableSetter table) {
        super(table);
        this.packageName = daoImplPackage(table);
        this.klassName = daoImplClass(table);
    }

    public static TypeName daoImplementName(TableSetter table) {
        return ClassName.get(daoImplPackage(table), daoImplClass(table));
    }

    public static String daoImplPackage(TableSetter table) {
        return table.getBasePackage() + ".dao.impl";
    }

    public static String daoImplClass(TableSetter table) {
        return table.getEntityPrefix() + "DaoImpl";
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.addAnnotation(Repository);
        builder.superclass(ClassName.get(
            table.getBasePackage() + ".dao.base",
            table.getEntityPrefix() + "BaseDao"));
        builder.addSuperinterface(DaoInterfaceFile.daoInterfaceName(table));
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}
