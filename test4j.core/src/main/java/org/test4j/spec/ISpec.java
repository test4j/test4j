package org.test4j.spec;

/**
 * JSpec接口类
 * 
 * @author darui.wudr 2013-1-10 下午4:15:50
 */
@SuppressWarnings("rawtypes")
public interface ISpec {

    /**
     * 返回指定Steps的实例
     * 
     * @param clazzName
     * @return
     */
    Steps getStepsInstance(String stepClazzName);

    /**
     * 返回共享数据设置类
     * 
     * @return
     */
    SharedData getSharedData();
}
