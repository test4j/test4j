package org.test4j.module.spec.internal;

import org.test4j.Context;
import org.test4j.tools.datagen.IDataMap;

/**
 * IAroundInitial
 *
 * @author:darui.wu Created by darui.wu on 2020/4/14.
 */
public interface IAroundInitial extends IWhen, IAroundHandler {

    /**
     * 执行 前置数据准备 和 后置数据检查
     * 文件名形式为 testClassName.testMethodName.json
     *
     * @return
     * @throws RuntimeException
     */
    default IAroundInitial dbAround() throws RuntimeException {
        try {
            String file = TableDataAround.findFile(Context.currTestClass(), Context.currTestMethod().getName());
            return this.dbAround(file);
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - 数据库初始化数据和检查数据准备失败：" + e.getMessage(), e);
        }
    }

    /**
     * 执行 前置数据准备 和 后置数据检查
     *
     * @param file json文件
     * @return
     * @throws RuntimeException
     */
    default IAroundInitial dbAround(String file) throws RuntimeException {
        TableDataAround.around(file);
        return this;
    }

    /**
     * 按 readyInitial 初始化准备数据
     * 按 checkInitial 初始化检查数据
     * init方法用来提供默认字段数据，如果json文件中定义对应的字段，以json文件数据为准
     *
     * @param readyInitial 初始化准备数据
     * @param checkInitial 初始化检查数据
     * @param tables       指定表，如果未指定，表示所有表
     * @return
     */
    default IAroundInitial initAround(IDataMap readyInitial, IDataMap checkInitial, String... tables) {
        TableDataAround.initReady(readyInitial, tables);
        TableDataAround.initCheck(checkInitial, tables);
        return this;
    }
}