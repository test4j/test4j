package org.test4j.module.database.utility;

/**
 * @author wudarui
 */
public enum StatementStatus {
    /**
     * 正常语句
     */
    NORMAL, // <br>
    /**
     * 单行注释
     **/
    SINGLE_NOTE, // <br>
    /**
     * 多行注释
     **/
    MULTI_NOTE, // <br>
    /**
     * 单引号内容
     **/
    SINGLE_QUOTATION, // <br>
    /**
     * 双引号内容
     **/
    DOUBLE_QUOTATION;
}
