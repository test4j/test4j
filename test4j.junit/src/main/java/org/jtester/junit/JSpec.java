package org.jtester.junit;

import java.util.Map;

import org.jtester.junit.annotations.DataFrom;
import org.jtester.module.core.ICoreInitial;
import org.jtester.module.core.TestContext;
import org.jtester.module.jmockit.utility.JMockitModuleHelper;
import org.jtester.spec.ISpec;
import org.jtester.spec.ISpecExecutorFactory;
import org.jtester.spec.SharedData;
import org.jtester.spec.Steps;
import org.jtester.spec.inner.IScenario;
import org.jtester.spec.inner.ISpecMethod;
import org.jtester.spec.inner.ISpecMethod.SpecMethodID;
import org.jtester.spec.inner.ISpecPrinter;
import org.jtester.tools.datagen.DataProviderIterator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * junit版本jspec
 * 
 * @author darui.wudr 2013-1-10 下午4:16:42
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class JSpec implements ISpec, JTester {
    static ISpecExecutorFactory                  specFactory   = ICoreInitial.initSpecExecutorFactory();

    private final Map<SpecMethodID, ISpecMethod> specMethods;

    private Map<String, Steps>                   stepsInstances;

    private static ThreadLocal<ISpecPrinter>     threadPrinter = new ThreadLocal<ISpecPrinter>();

    public JSpec() {
        this.specMethods = specFactory.findMethodsInSpec(this.getClass());
    }

    protected SharedData shared;

    /**
     * 用来初始化共享的数据结构<br>
     * 允许子类进行覆盖
     */
    protected void initSharedData() {
    }

    @Override
    public final SharedData getSharedData() {
        if (shared == null) {
            shared = new SharedData.EmptyData();
        }
        return shared;
    }

    @Override
    public final Steps getStepsInstance(String stepClazzName) {
        return this.stepsInstances.get(stepClazzName);
    }

    @BeforeClass
    public static void initSpecPrinter() {
        threadPrinter.set(specFactory.newSpecPrinter());
    }

    @AfterClass
    public static void cleanSpecPrinter() {
        getPrinter().printSummary(TestContext.currTestedClazz());
        threadPrinter.remove();
    }

    static ISpecPrinter getPrinter() {
        ISpecPrinter printer = threadPrinter.get();
        if (printer == null) {
            printer = specFactory.newSpecPrinter();
            threadPrinter.set(printer);
        }
        return printer;
    }

    /**
     * 场景测试方法入口，执行多个场景
     * 
     * @param scenario
     * @throws Throwable
     */
    @Test
    @DataFrom("scenariosOfStorySpec")
    public void runScenario(IScenario scenario) throws Throwable {
        this.initSharedData();
        this.stepsInstances = specFactory.newSteps(this);
        specFactory.runScenario(this, scenario, specMethods, getPrinter());
    }

    final DataProviderIterator<IScenario> scenariosOfStorySpec() {
        DataProviderIterator<IScenario> it = specFactory.findScenario(this.getClass());
        return it;
    }

    static {
        JMockitModuleHelper.getJMockitJavaagentHit();
    }
}
