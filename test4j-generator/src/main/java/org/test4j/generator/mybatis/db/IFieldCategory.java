package org.test4j.generator.mybatis.db;

import lombok.Getter;

/**
 * IFieldCategory
 *
 * @author wudarui
 */
public enum IFieldCategory {
    /**
     * 主键
     */
    PrimaryKey(0),
    /**
     * 自增主键
     */
    PrimaryId(0),
    /**
     * gmt_create字段
     */
    GmtCreate(1),
    /**
     * gmt_modified字段
     */
    GmtModified(2),
    /**
     * 逻辑删除字段
     */
    IsDeleted(3),
    /**
     * 普通字段
     */
    Common(4);

    /**
     * 字段排序
     */
    @Getter
    private int order;

    IFieldCategory(int order) {
        this.order = order;
    }
}