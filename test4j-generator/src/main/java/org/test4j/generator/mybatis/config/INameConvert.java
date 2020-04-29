package org.test4j.generator.mybatis.config;

import org.test4j.generator.mybatis.model.TableField;
import org.test4j.generator.mybatis.model.TableInfo;

/**
 * 名称转换接口类
 *
 * @author hubin
 * @since 2017-01-20
 */
public interface INameConvert {

    /**
     * 执行实体名称转换
     *
     * @param tableInfo 表信息对象
     * @return
     */
    String entityNameConvert(TableInfo tableInfo);

    /**
     * 执行属性名称转换
     *
     * @param field 表字段对象，如果属性表字段命名不一致注意 convert 属性的设置
     * @return
     */
    String propertyNameConvert(TableField field);
}
