package org.test4j.module.spec.internal;

import org.test4j.tools.datagen.TableMap;
import org.test4j.tools.datagen.TableDataAround;

import java.util.function.Consumer;

/**
 * IAroundHandler
 *
 * @author:darui.wu Created by darui.wu on 2020/4/14.
 */
public interface IAroundHandler extends IWhen {

    /**
     * 按 readyHandler 加工准备数据
     * 按 checkHandler 加工检查数据
     * 对 从json文件中加载进来的数据进行处理，如果改写了json中的数据，以处理后的数据为准
     *
     * @param readyHandler ready数据加工
     * @param checkHandler check数据加工
     * @return
     */
    default IAroundHandler handleAround(Consumer<TableMap> readyHandler, Consumer<TableMap> checkHandler) {
        TableDataAround.initReady(readyHandler);
        TableDataAround.initCheck(checkHandler);
        return this;
    }
}
