package org.test4j.generator.javafile;

import com.squareup.javapoet.*;
import org.test4j.generator.config.impl.TableSetter;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BaseFile {
    protected TableSetter table;

    protected String packageName;

    protected String klassName;

    protected String comment;

    public BaseFile(TableSetter table) {
        this.table = table;
    }

    protected ClassName className() {
        return ClassName.get(packageName, klassName);
    }

    /**
     * 生成java文件
     *
     * @param srcDir 代码src路径
     */
    public final void javaFile(String srcDir, boolean forceWrite) {
        this.javaFile(new File(srcDir), forceWrite);
    }

    /**
     * 生成java文件
     *
     * @param srcDir     代码src路径
     * @param forceWrite 重写
     */
    public final void javaFile(File srcDir, boolean forceWrite) {
        if (!forceWrite && new File(srcDir + this.filePath()).exists()) {
            System.out.println("=========文件:" + this.klassName + ".java已经存在, 根据配置跳过重写=========");
            return;
        }
        TypeSpec.Builder builder;
        if (this.isInterface()) {
            builder = TypeSpec.interfaceBuilder(klassName).addModifiers(Modifier.PUBLIC);
        } else {
            builder = TypeSpec.classBuilder(klassName).addModifiers(Modifier.PUBLIC);
        }
        this.build(builder);
        CodeBlock comment = this.codeBlock("",
            this.klassName + (this.comment == null ? "" : ": " + this.comment),
            "",
            "@author darui.wu@163.com"
        );
        builder.addJavadoc(comment);
        JavaFile.Builder javaBuilder = JavaFile.builder(packageName, builder.build());
        this.staticImport(javaBuilder);
        JavaFile javaFile = javaBuilder.build();

        try {
            javaFile.writeTo(srcDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String filePath() {
        return "/" + this.packageName.replace('.', '/') + "/" + this.klassName + ".java";
    }

    protected void staticImport(JavaFile.Builder builder) {
    }

    /**
     * 代码块, 或者注释块
     *
     * @param lines 代码行
     * @return
     */
    protected CodeBlock codeBlock(String... lines) {
        return CodeBlock.join(Stream.of(lines).map(CodeBlock::of).collect(Collectors.toList()), "\n");
    }

    protected CodeBlock of(String format, Object... args) {
        return CodeBlock.of(format.replace('\'', '"'), args);
    }

    protected CodeBlock codeBlock(CodeBlock... blocks) {
        return CodeBlock.join(Stream.of(blocks).collect(Collectors.toList()), "\n");
    }

    protected abstract void build(TypeSpec.Builder builder);

    protected TypeName parameterizedType(ClassName raw, TypeName... paras) {
        return ParameterizedTypeName.get(raw, paras);
    }

    protected TypeName parameterizedType(Class raw, Class... paras) {
        return ParameterizedTypeName.get(raw, paras);
    }

    /**
     * 是否接口类
     *
     * @return
     */
    protected abstract boolean isInterface();
}
