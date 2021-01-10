package org.test4j.module.spec;

import org.test4j.ISpring;
import org.test4j.asserts.IWant;
import org.test4j.module.spec.internal.SpecContext;
import org.test4j.tools.IUtils;

/**
 * JSpec步骤定义文件接口 <br>
 * 标识接口
 *
 * @author darui.wudr 2013-6-3 下午7:04:36
 */
public interface IMix<T> extends ISpring, IWant, IUtils {
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