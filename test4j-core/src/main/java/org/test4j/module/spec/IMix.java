package org.test4j.module.spec;

import org.test4j.ICore;
import org.test4j.ISpring;
import org.test4j.module.database.IDatabase;
import org.test4j.module.spec.internal.SpecContext;

/**
 * JSpec步骤定义文件接口 <br>
 * 标识接口
 *
 * @author darui.wudr 2013-6-3 下午7:04:36
 */
public interface IMix<T> extends ICore, ISpring, IDatabase {
    /**
     * 设置共享数据
     *
     * @param data
     */
    default void setData(T data) {
        SpecContext.setSharedData(data);
    }

    /**
     * 返回共享数据
     *
     * @return
     */
    default T getData() {
        return (T) SpecContext.getSharedData();
    }
}