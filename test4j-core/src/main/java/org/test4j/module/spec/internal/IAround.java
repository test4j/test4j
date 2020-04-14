package org.test4j.module.spec.internal;


import org.test4j.tools.datagen.IDataMap;
import org.test4j.tools.datagen.TableDataAround;

/**
 * @Descriotion:
 * @param:
 * @return:
 * @author:darui.wu Created by darui.wu on 2020/4/14.
 */
public interface IAround extends IWhen {
    /**
     * 按DataMap 初始化准备数据
     *
     * @param data
     * @param tables
     * @return
     */
    default IAround initReady(IDataMap data, String... tables) {
        TableDataAround.initReady(data, tables);
        return this;
    }

    /**
     * 按DataMap 初始化检查数据
     *
     * @param data
     * @param tables
     * @return
     */
    default IAround initCheck(IDataMap data, String... tables) {
        TableDataAround.initCheck(data, tables);
        return this;
    }
}