package org.test4j.generator.template;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.test4j.generator.config.constant.OutputDir;
import org.test4j.generator.config.impl.TableSetter;

import java.util.HashMap;
import java.util.Map;

import static org.test4j.generator.config.constant.ConfigKey.KEY_NAME;
import static org.test4j.generator.config.constant.ConfigKey.KEY_PACKAGE;

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
     * @return
     */
    public final void initContext(TableSetter table, Map<String, Object> parent, Map<String, Object> ctx) {
        this.filePath = table.outputDir(this.outputDir) + this.fileNameReg.replace("*", table.getEntityPrefix());

        ctx.put(KEY_NAME, this.getFileName(table));
        ctx.put(KEY_PACKAGE, this.getPackage(table));

        this.templateConfigs(table, parent, ctx);
    }

    /**
     * 模板code
     *
     * @return
     */
    public abstract String getTemplateId();

    /**
     * 模板自身的配置项
     *
     * @param table
     * @param ctx   模板自身构建的上下文
     * @return
     */
    protected abstract void templateConfigs(TableSetter table, Map<String, Object> parent, Map<String, Object> ctx);

    protected String getFileName(TableSetter table) {
        int start = this.fileNameReg.lastIndexOf('/');
        int end = this.fileNameReg.lastIndexOf('.');
        return this.fileNameReg.substring(start + 1, end).replace("*", table.getEntityPrefix());
    }

    protected String getPackage(TableSetter table) {
        int index = this.fileNameReg.lastIndexOf('/');
        String sub = "";
        if (index > 0) {
            sub = this.fileNameReg.substring(0, index).replace('/', '.');
        }
        return table.getBasePackage() + "." + sub;
    }

    /**
     * 从context中获取ongl配置值
     *
     * @param context  上下文
     * @param key      ongl表达式 key1.key2
     * @param _default 默认值
     * @return 如果key值不存在, 且默认值为null, 则抛出异常
     */
    public static String getConfig(Map<String, Object> context, String key, String _default) {
        Object temp = context;
        String[] keys = key.split("\\.");
        for (String item : keys) {
            if (temp instanceof Map) {
                temp = ((Map) temp).get(item);
            } else {
                throw new RuntimeException("the key[" + key + "] not found.");
            }
        }
        if (temp instanceof String) {
            return (String) temp;
        }
        if (temp == null && _default != null) {
            return _default;
        } else {
            throw new RuntimeException("the key[" + key + "] not found.");
        }
    }

    /**
     * 替换变量 ${entity}, ${query}, ${update}
     *
     * @param str
     * @param context
     * @return
     */
    protected String replace(String str, Map<String, Object> context, String... vars) {
        String replaced = str;
        for (String var : vars) {
            if (VAR_MAPPING.containsKey(var)) {
                replaced = replaced.replace(var, getConfig(context, VAR_MAPPING.get(var), null));
            }
        }
        return replaced;
    }

    static final Map<String, String> VAR_MAPPING = new HashMap<>();

    static {
        VAR_MAPPING.put("${entity}", "entity.name");
        VAR_MAPPING.put("${query}", "entityQuery.name");
        VAR_MAPPING.put("${update}", "entityUpdate.name");
    }
}