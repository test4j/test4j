package org.test4j.generator.mybatis.model;

/**
 * 获取字段属性类型
 *
 * @author wudarui
 */
public interface IJavaType {

    /**
     * 获取字段类型
     *
     * @return 字段类型
     */
    String getFieldType();

    /**
     * 获取字段完整路径类型
     *
     * @return 字段类型完整名
     */
    String getImportName();
}
