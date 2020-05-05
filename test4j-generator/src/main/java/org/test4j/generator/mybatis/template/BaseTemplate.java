package org.test4j.generator.mybatis.template;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.test4j.generator.mybatis.config.OutputDir;
import org.test4j.generator.mybatis.model.TableInfo;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.test4j.generator.mybatis.config.ConfigKey.*;

/**
 * 根据表信息生成文件
 *
 * @author darui.wu
 */
@Data
@Accessors(chain = true)
public abstract class BaseTemplate {
    /**
     * 模板内容
     */
    protected String template;
    /**
     * 生成的文件路径
     */
    protected String filePath;

    protected String fileNameReg;

    @Getter(AccessLevel.NONE)
    protected OutputDir outputDir = OutputDir.Base;
    /**
     * 是否分库
     */
    @Setter
    private boolean isPartition = false;

    private BaseTemplate() {
    }

    public BaseTemplate(String template, String fileNameReg) {
        this.fileNameReg = fileNameReg;
        this.template = template;
    }

    /**
     * 使用表信息初始化模板变量
     *
     * @param table
     * @param configs
     * @return
     */
    public final Map<String, Object> initContext(TableInfo table) {
        this.filePath = table.outputDir(this.outputDir) + this.fileNameReg.replace("*", table.getEntityPrefix());
        Map<String, Object> context = this.templateConfigs(table);
        if (context == null) {
            context = new HashMap<>();
        }
        if (!context.containsKey(KEY_NAME)) {
            context.put(KEY_NAME, this.getFileName(table));
        }
        if (!context.containsKey(KEY_PACKAGE)) {
            context.put(KEY_PACKAGE, this.getPackage(table));
        }
        return context;
    }

    public abstract String getTemplateId();

    /**
     * 模板自身的配置项
     *
     * @param table
     * @return
     */
    protected abstract Map<String, Object> templateConfigs(TableInfo table);

    private String getFileName(TableInfo table) {
        int start = this.fileNameReg.lastIndexOf('/');
        int end = this.fileNameReg.lastIndexOf('.');
        return this.fileNameReg.substring(start + 1, end).replace("*", table.getEntityPrefix());
    }

    private String getPackage(TableInfo table) {
        int index = this.fileNameReg.lastIndexOf('/');
        String sub = "";
        if (index > 0) {
            sub = this.fileNameReg.substring(0, index).replace('/', '.');
        }
        return table.getBasePackage() + "." + sub;
    }
}
