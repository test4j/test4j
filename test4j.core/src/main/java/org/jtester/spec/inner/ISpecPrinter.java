package org.jtester.spec.inner;

import org.jtester.spec.ISpec;

/**
 * 测试场景结果输出器
 * 
 * @author darui.wudr 2013-1-10 下午6:59:24
 */
public interface ISpecPrinter {
    /**
     * 输出整个测试场景列表
     * 
     * @param spec
     */
    void printSummary(Class<? extends ISpec> spec);

    /**
     * 输出测试场景信息
     * 
     * @param scenario
     */
    void printScenario(ISpec spec, IScenario scenario);
}
