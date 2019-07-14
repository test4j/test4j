package org.test4j.module.core.internal;

import static org.test4j.tools.commons.ConfigHelper.getBoolean;

/**
 * test4j常用配置项
 * 
 * @author darui.wudr
 */
public interface IPropConst {
    /**
     * 允许将所有spring bean都设置成lazy加载模式
     */
    final boolean SPRING_LAZY_LOAD              = getBoolean("spring.lazy.load");

    final boolean IBATIS_SQLMAP_THROW_EXCEPTION = getBoolean("ibatis.sqlmap.throw.exception");
    /**
     * 是否打开测试运行信息跟踪功能
     */
    final boolean TRACER_ENABLE                 = getBoolean("tracer.enabled");
}
