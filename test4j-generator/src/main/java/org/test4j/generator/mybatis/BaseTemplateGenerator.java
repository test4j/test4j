package org.test4j.generator.mybatis;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.test4j.generator.engine.AbstractTemplateEngine;
import org.test4j.generator.engine.VelocityTemplateEngine;
import org.test4j.generator.mybatis.config.GlobalConfig;
import org.test4j.generator.mybatis.config.TableConfig;
import org.test4j.generator.mybatis.config.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;
import org.test4j.hamcrest.Assert;
import org.test4j.tools.commons.StringHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.test4j.generator.mybatis.config.constant.ConfigKey.*;
import static org.test4j.module.core.utility.MessageHelper.info;

/**
 * @author darui.wu
 */
@Slf4j
@Getter
@Accessors(chain = true)
public abstract class BaseTemplateGenerator<G extends BaseTemplateGenerator> implements TemplateGenerator {
    @Getter(AccessLevel.NONE)
    protected AbstractTemplateEngine templateEngine = new VelocityTemplateEngine();

    protected List<TableConfig> tableConfigs = new ArrayList<>();
    /**
     * 全局配置
     */
    @Setter
    protected GlobalConfig globalConfig;

    @Override
    public TemplateGenerator globalConfig(Consumer<GlobalConfig> consumer) {
        this.globalConfig = new GlobalConfig();
        consumer.accept(this.globalConfig);
        return this;
    }

    @Override
    public TemplateGenerator tables(Consumer<TableConfig> consumer) {
        TableConfig tableConfig = new TableConfig();
        consumer.accept(tableConfig);
        tables(tableConfig);
        return (G) this;
    }

    public TemplateGenerator tables(TableConfig... tableConfigs) {
        for (TableConfig tableConfig : tableConfigs) {
            tableConfig.setGlobalConfig(globalConfig);
            this.tableConfigs.add(tableConfig);
        }
        return (G) this;
    }

    /**
     * 返回定义的模板列表
     *
     * @return
     */
    protected abstract List<BaseTemplate> getAllTemplates();

    /**
     * 执行代码生成
     */
    @Override
    public void execute() {
        if (globalConfig == null) {
            throw new RuntimeException("the global config not set.");
        }
        if (tableConfigs == null || tableConfigs.isEmpty()) {
            throw new RuntimeException("the table config not set.");
        }
        List<Map<String, Object>> allContext = new ArrayList<>();
        for (TableConfig config : this.tableConfigs) {
            info("===数据库元信息初始化...");
            config.setGlobalConfig(this.globalConfig).initTables();
            info("===准备生成文件...");
            for (Map.Entry<String, TableInfo> entry : config.getTables().entrySet()) {
                info("======处理表：" + entry.getKey());
                TableInfo table = entry.getValue();
                Map<String, Object> context = this.getAllTemplateContext(table);
                this.generateTemplates(table, context);
                allContext.add(context);
            }
            info("===文件生成完成！！！");
        }
        this.generateSummary(allContext);
        this.open();
    }

    /**
     * 生成汇总文件
     *
     * @param allContext
     */
    protected void generateSummary(List<Map<String, Object>> allContext) {
    }

    /**
     * 生成模板文件
     *
     * @param table
     * @param context
     */
    private void generateTemplates(TableInfo table, Map<String, Object> context) {
        this.getAllTemplates().stream()
            .filter(template -> !template.isPartition() || table.isPartition())
            .forEach(template -> {
                String filePath = template.getFilePath();
                info("=========生成文件: " + template.getFileNameReg());
                Assert.notNull(filePath, "文件路径不能为空,[table=%s,template=%s]", table.getTableName(), template.getTemplate());
                templateEngine.output(template.getTemplate(), context, filePath);
            });
    }

    /**
     * 初始化所有模板的上下文变量
     *
     * @param table
     * @return
     */
    private Map<String, Object> getAllTemplateContext(TableInfo table) {
        final Map<String, Object> context = table.initTemplateContext();
        this.getAllTemplates().forEach(template -> {
                Map<String, Object> templateContext = template.initContext(table);
                if (KEY_ENTITY.equals(template.getTemplateId())) {
                    context.put(KEY_ENTITY_NAME, templateContext.get(KEY_NAME));
                }
                context.put(template.getTemplateId(), templateContext);
            }
        );
        return context;
    }


    /**
     * 打开输出目录
     */
    private void open() {
        try {
            if (globalConfig.isOpen() && !StringHelper.isBlank(globalConfig.getOutputDir())) {
                String osName = System.getProperty("os.name");
                if (osName != null) {
                    if (osName.contains("Mac")) {
                        Runtime.getRuntime().exec("open " + globalConfig.getOutputDir());
                    } else if (osName.contains("Windows")) {
                        Runtime.getRuntime().exec("cmd /c start " + globalConfig.getOutputDir());
                    } else {
                        log.debug("文件输出目录:" + globalConfig.getOutputDir());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}