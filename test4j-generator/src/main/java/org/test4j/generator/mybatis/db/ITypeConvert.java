package org.test4j.generator.mybatis.db;

import org.test4j.generator.mybatis.config.TableField;

/**
 * 数据库字段类型转换
 *
 * @author darui.wu
 */
public interface ITypeConvert {


    /**
     * 执行类型转换
     *
     * @param dateType   时间转换
     * @param tableField 字段列信息
     * @return 字段类型
     */
    default IJavaType processTypeConvert(DateType dateType, TableField tableField) {
        // 该方法提供重写
        return processTypeConvert(dateType, tableField.getColumnType());
    }


    /**
     * 执行类型转换
     *
     * @param dateType  时间转换
     * @param fieldType 字段类型
     * @return 字段类型
     */
    IJavaType processTypeConvert(DateType dateType, String fieldType);
}
