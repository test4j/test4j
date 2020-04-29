package org.test4j.generator.mybatis.model;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.test4j.generator.mybatis.config.StrategyConfig;
import org.test4j.generator.mybatis.rule.IColumnType;
import org.test4j.generator.mybatis.rule.Naming;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

import static cn.org.atool.fluent.mybatis.generator.MybatisGenerator.currTable;

/**
 * 表字段信息
 *
 * @author YangHu
 * @since 2016-12-03
 */
@Getter
@Accessors(chain = true)
public class TableField {
    @Setter
    private boolean convert;
    @Setter
    private boolean keyFlag;
    /**
     * 主键是否为自增类型
     */
    @Setter
    private boolean keyIdentityFlag;
    @Setter
    private String name;
    @Setter
    private String type;
    @Setter
    private String propertyName;

    private IColumnType columnType;

    public void setColumnType(IColumnType columnType) {
        IColumnType specType = currTable().columnType(name);
        this.columnType = specType == null ? columnType : specType;
    }

    @Setter
    private String comment;
    @Setter
    private String fill;
    /**
     * 自定义查询字段列表
     */
    @Setter
    private Map<String, Object> customMap;

    protected TableField setConvert(StrategyConfig strategyConfig) {
        if (strategyConfig.isEntityTableFieldAnnotationEnable()) {
            this.convert = true;
            return this;
        }
        if (strategyConfig.isCapitalModeNaming(name)) {
            this.convert = false;
        } else {
            // 转换字段
            if (Naming.underline_to_camel == strategyConfig.getColumnNaming()) {
                // 包含大写处理
                if (StringUtils.containsUpperCase(name)) {
                    this.convert = true;
                }
            } else if (!name.equals(propertyName)) {
                this.convert = true;
            }
        }
        return this;
    }

    public TableField setPropertyName(StrategyConfig strategyConfig, String propertyName) {
        this.propertyName = propertyName;
        this.setConvert(strategyConfig);
        return this;
    }

    public String getPropertyType() {
        if (null != columnType) {
            return columnType.getPropertyType();
        }
        return null;
    }

    /**
     * 按JavaBean规则来生成get和set方法
     */
    public String getCapitalName() {
        if (propertyName.length() <= 1) {
            return propertyName.toUpperCase();
        }

        // 第一个字母小写， 第二个字母大写，特殊处理
        String firstChar = propertyName.substring(0, 1);
        if (Character.isLowerCase(firstChar.toCharArray()[0]) && Character.isUpperCase(propertyName.substring(1, 2).toCharArray()[0])) {
            return firstChar.toLowerCase() + propertyName.substring(1);
        } else {
            return firstChar.toUpperCase() + propertyName.substring(1);
        }
    }
}
