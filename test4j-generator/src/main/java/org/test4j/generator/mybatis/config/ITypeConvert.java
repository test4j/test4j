package org.test4j.generator.mybatis.config;

import org.test4j.generator.mybatis.model.BuildConfig;
import org.test4j.generator.mybatis.model.TableField;
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
     * @param dateType   时间转换
     * @param tableField 字段列信息
     * @return 字段类型
     */
    default IColumnType processTypeConvert(DateType dateType, TableField tableField) {
        // 该方法提供重写
        return processTypeConvert(dateType, tableField.getFieldType());
    }


    /**
     * 执行类型转换
     *
     * @param dateType  时间转换
     * @param fieldType 字段类型
     * @return 字段类型
     */
    IColumnType processTypeConvert(DateType dateType, String fieldType);
}
