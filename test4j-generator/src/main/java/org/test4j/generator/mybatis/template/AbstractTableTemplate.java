package org.test4j.generator.mybatis.template;

import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.test4j.generator.engine.AbstractTemplateEngine;
import org.test4j.generator.mybatis.model.BuildConfig;
import org.test4j.generator.mybatis.model.TableInfo;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 根据表信息生成文件
 *
 * @author darui.wu
 */
@Data
@Accessors(chain = true)
public abstract class AbstractTableTemplate {
    /**
     * 模板内容
     */
    protected String template;
    /**
     * 生成的文件路径
     */
    protected String filePath;
    /**
     * 文件名称(不包含路径和后缀)
     */
    private String fileName;

    private String fileNameReg;

    private TemplateType templateType = TemplateType.Base;
    /**
     * 是否base dao
     */
    @Setter
    private boolean isBaseDao = false;
    /**
     * 是否分库
     */
    @Setter
    private boolean isPartition = false;

    private AbstractTableTemplate() {
    }

    public AbstractTableTemplate(String template, String fileNameReg) {
        this.fileNameReg = fileNameReg;
        this.template = template;
    }

    public abstract void generate(BuildConfig config, TableInfo tableInfo);

    /**
     * 使用表信息初始化模板变量
     *
     * @param table
     * @return
     */
    public abstract Map<String, Object> initWith(TableInfo table);

    /**
     * 模板类型
     */
    public enum TemplateType {
        Base,
        Test,
        Dao;
    }
}
