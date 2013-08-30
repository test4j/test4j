package org.jtester.spec.inner;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.jtester.spec.inner.ISpecMethod.SpecMethodID;

/**
 * 场景步骤接口
 * 
 * @author darui.wudr 2013-1-10 下午7:03:47
 */
public interface IScenarioStep {
    /**
     * 本步骤是否被跳过
     * 
     * @return
     */
    boolean isSuspend();

    /**
     * 不步骤是否执行成功
     * 
     * @return
     */
    boolean isSuccess();

    /**
     * 是否是执行失败
     * 
     * @return
     */
    boolean isFailure();

    /**
     * 步骤类型
     * 
     * @return
     */
    StepType getType();

    /**
     * 返回场景步骤所对应的方法
     * 
     * @return
     */
    SpecMethodID getSpecMethodID();

    /**
     * 设置步骤错误（异常）
     * 
     * @param instance
     */
    void setError(Throwable error);

    /**
     * 返回错误信息
     * 
     * @return
     */
    Throwable getError();

    /**
     * 根据步骤中参数名称和类型返回参数列表
     * 
     * @param paraNames 参数名称列表
     * @param paraTypes 参数类型列表
     * @return
     */
    Object[] getArguments(List<String> paraNames, List<Type> paraTypes);

    /**
     * 返回场景步骤中的参数
     * 
     * @return
     */
    Map<String, String> getParas();

    /**
     * 返回步骤方法名称
     * 
     * @return
     */
    String getMethod();

    /**
     * 返回方法的描述信息 :文本 + 参数
     * 
     * @return
     */
    String getDisplayText();
}
