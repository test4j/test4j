package org.test4j.generator.mybatis.config;

import org.test4j.generator.mybatis.model.TableInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 输出文件配置
 *
 * @author hubin
 * @since 2017-01-18
 */
@Data
@Accessors(chain = true)
public abstract class FileOutConfig {

    /**
     * 模板
     */
    private String templatePath;

    public FileOutConfig() {
        // to do nothing
    }

    public FileOutConfig(String templatePath) {
        this.templatePath = templatePath;
    }

    /**
     * 输出文件
     */
    public abstract String outputFile(TableInfo tableInfo);
}
