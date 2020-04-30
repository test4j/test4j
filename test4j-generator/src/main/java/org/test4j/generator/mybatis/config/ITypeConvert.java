package org.test4j.generator.mybatis.config;

import org.test4j.generator.mybatis.model.TableField;
import org.test4j.generator.mybatis.rule.ColumnType;
import org.test4j.generator.mybatis.rule.DateType;
import org.test4j.generator.mybatis.rule.IColumnType;

/**
 * 数据库字段类型转换
 *
 * @author hubin
 * @since 2017-01-20
 */
public interface ITypeConvert {


    /**
     * 执行类型转换
     *
     * @param globalConfig 全局配置
     * @param tableField   字段列信息
     * @return ignore
     */
    default IColumnType processTypeConvert(GlobalConfig globalConfig, TableField tableField) {
        // 该方法提供重写
        return processTypeConvert(globalConfig, tableField.getType());
    }


    /**
     * 执行类型转换
     *
     * @param globalConfig 全局配置
     * @param fieldType    字段类型
     * @return ignore
     */
    IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType);
}
