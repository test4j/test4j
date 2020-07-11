package org.test4j.generator.mybatis.config;

import org.test4j.generator.mybatis.config.constant.DefinedColumn;
import org.test4j.generator.mybatis.db.ColumnJavaType;

import java.util.function.Consumer;

/**
 * ISetTableInfo 设置表信息
 *
 * @author:darui.wu Created by darui.wu on 2020/6/1.
 */
public interface ITableSetter {
    /**
     * 设置转为为Entity时, 需要去掉的表名前缀
     *
     * @param tablePrefix
     * @return
     */
    ITableSetter setTablePrefix(String... tablePrefix);

    /**
     * 指定特殊字段
     *
     * @param gmtCreate    记录创建时间
     * @param gmtModified  记录修改时间
     * @param logicDeleted 记录逻辑删除字段
     * @return
     */
    ITableSetter setColumn(String gmtCreate, String gmtModified, String logicDeleted);

    /**
     * 指定gmtCreate记录创建字段名
     *
     * @param gmtCreate
     * @return
     */
    ITableSetter setGmtCreate(String gmtCreate);

    /**
     * 指定gmtModified记录修改字段名
     *
     * @param gmtModified
     * @return
     */
    ITableSetter setGmtModified(String gmtModified);

    /**
     * 指定logicDeleted记录逻辑删除字段名
     *
     * @param logicDeleted
     * @return
     */
    ITableSetter setLogicDeleted(String logicDeleted);

    /**
     * 指定字段类型
     *
     * @param columnName 字段名
     * @param javaType   Entity字段类型
     * @return
     */
    ITableSetter setColumnType(String columnName, ColumnJavaType javaType);

    /**
     * 指定字段对应的Entity属性名称
     *
     * @param columnName   字段名称
     * @param propertyName Entity属性名称
     * @return
     */
    ITableSetter setColumn(String columnName, String propertyName);

    /**
     * 指定字段对应的Entity属性名称及类型
     *
     * @param columnName   字段名称
     * @param propertyName Entity属性名称
     * @param javaType     Entity字段类型
     * @return
     */
    ITableSetter setColumnType(String columnName, String propertyName, ColumnJavaType javaType);

    /**
     * 设置大字段列表
     *
     * @param column   字段
     * @param consumer 指定字段属性
     * @return
     */
    ITableSetter setColumn(String column, Consumer<DefinedColumn> consumer);

    /**
     * 排除字段, 被排除的字段不生成Entity属性
     *
     * @param columnNames 被排除的字段
     * @return
     */
    ITableSetter setExcludes(String... columnNames);

    /**
     * 设置生成的base dao类需要实现的接口
     *
     * @param interfaceFullName     接口类名称（包括package路径）
     * @param parameterGenericTypes 接口泛型
     * @return
     */
    ITableSetter addBaseDaoInterface(String interfaceFullName, String... parameterGenericTypes);

    /**
     * 设置生成的base dao类需要实现的接口
     *
     * @param interfaceType         接口类型
     * @param parameterGenericTypes 接口泛型
     * @return
     */
    ITableSetter addBaseDaoInterface(Class interfaceType, String... parameterGenericTypes);

    /**
     * 增加Entity类的接口
     *
     * @param interfaceType
     * @param parameterGenericTypes 接口泛型
     * @return
     */
    ITableSetter addEntityInterface(String interfaceType, String... parameterGenericTypes);

    /**
     * 增加Entity类的接口
     *
     * @param interfaceType
     * @param parameterGenericTypes 接口泛型
     * @return
     */
    ITableSetter addEntityInterface(Class interfaceType, String... parameterGenericTypes);

    /**
     * 设置生成的Mapper类的Spring bean名称前缀
     *
     * @param mapperBeanPrefix mapper bean名称前缀
     * @return
     */
    ITableSetter setMapperPrefix(String mapperBeanPrefix);

    /**
     * 设置成分库分表形式
     *
     * @return
     */
    ITableSetter enablePartition();

    /**
     * 设置表主键生成对应的seqName
     *
     * @param seqName
     * @return
     */
    ITableSetter setSeqName(String seqName);
}