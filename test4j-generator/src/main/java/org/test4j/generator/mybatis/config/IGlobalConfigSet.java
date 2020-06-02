package org.test4j.generator.mybatis.config;

import org.test4j.generator.mybatis.db.ITypeConvert;

/**
 * IGlobalConfigSet 全局配置
 *
 * @author:darui.wu Created by darui.wu on 2020/6/1.
 */
public interface IGlobalConfigSet {
    /**
     * 设置生成的文件输出目录(不包括package名称, 只指定src目录)
     *
     * @param outputDir 文件输出目录
     * @return
     */
    IGlobalConfigSet setOutputDir(String outputDir);

    /**
     * 设置生成的文件输出目录(不包括package名称, 只指定src目录)
     *
     * @param outputDir     mapper,bean等文件输出路径
     * @param testOutputDir 测试辅助文件（TableDataMap, EntityDataMap, TableMix等文件输出路径)
     * @param daoOutputDir  dao文件输出路径
     * @return
     */
    IGlobalConfigSet setOutputDir(String outputDir, String testOutputDir, String daoOutputDir);

    /**
     * 设置生成fluent mybatis文件的数据库链接信息
     *
     * @param url      数据库链接url
     * @param username 用户名
     * @param password 密码
     * @return
     */
    IGlobalConfigSet setDataSource(String url, String username, String password);

    /**
     * 设置生成fluent mybatis文件的数据库链接信息
     *
     * @param url         数据库链接url
     * @param username    用户名
     * @param password    密码
     * @param typeConvert 指定的类型转换
     * @return
     */
    IGlobalConfigSet setDataSource(String url, String username, String password, ITypeConvert typeConvert);

    /**
     * 生成的fluent mybatis的基础包路径
     *
     * @param basePackage
     */
    IGlobalConfigSet setBasePackage(String basePackage);
}