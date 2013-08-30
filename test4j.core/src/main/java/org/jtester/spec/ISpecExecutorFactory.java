package org.jtester.spec;

import java.util.Map;

import org.jtester.spec.inner.IScenario;
import org.jtester.spec.inner.ISpecMethod;
import org.jtester.spec.inner.ISpecMethod.SpecMethodID;
import org.jtester.spec.inner.ISpecPrinter;
import org.jtester.tools.datagen.DataProviderIterator;

/**
 * 场景测试工厂方法 <br>
 * 需要在配置文件中声明，或自定义覆盖
 * 
 * @author darui.wudr 2013-1-10 下午8:02:51
 */
public interface ISpecExecutorFactory {
    /**
     * 构造测试场景信息输出器
     * 
     * @return
     */
    ISpecPrinter newSpecPrinter();

    /**
     * 查找spec测试类中的步骤方法
     * 
     * @param clazz
     * @return
     */
    Map<SpecMethodID, ISpecMethod> findMethodsInSpec(Class<? extends ISpec> clazz);

    /**
     * 初始化Steps实例
     * 
     * @param spec
     * @return
     */
    Map<String, Steps> newSteps(ISpec spec);

    /**
     * 查找可以（或需要）运行的场景列表
     * 
     * @param clazz
     * @return
     */
    DataProviderIterator<IScenario> findScenario(Class<? extends ISpec> clazz);

    /**
     * 执行场景
     * 
     * @param spec 场景方法定义对象
     * @param scenario 要执行的场景
     * @param methods 场景方法列表
     * @throws Throwable
     */
    void runScenario(ISpec spec, IScenario scenario, Map<SpecMethodID, ISpecMethod> methods, ISpecPrinter printer)
            throws Throwable;
}
