package org.test4j.generator.mybatis.db;

/**
 * IFieldCategory
 *
 * @author wudarui
 */
public enum IFieldCategory {
    /**
     * 主键
     */
    PrimaryKey,
    /**
     * 自增主键
     */
    PrimaryId,
    /**
     * gmt_create字段
     */
    GmtCreate,
    /**
     * gmt_modified字段
     */
    GmtModified,
    /**
     * 逻辑删除字段
     */
    IsDeleted,
    /**
     * 普通字段
     */
    Common;
}