package org.jtester.testng;

import java.util.Map;

import org.jtester.module.ICore;
import org.jtester.module.core.ICoreInitial;
import org.jtester.module.core.TestContext;
import org.jtester.module.database.IDatabase;
import org.jtester.module.jmockit.IMockict;
import org.jtester.module.spring.ISpring;
import org.jtester.spec.ISpec;
import org.jtester.spec.ISpecExecutorFactory;
import org.jtester.spec.SharedData;
import org.jtester.spec.Steps;
import org.jtester.spec.inner.IScenario;
import org.jtester.spec.inner.ISpecMethod;
import org.jtester.spec.inner.ISpecMethod.SpecMethodID;
import org.jtester.spec.inner.ISpecPrinter;
import org.jtester.tools.datagen.DataProviderIterator;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class JSpec extends JTesterCore implements ISpec, ICore, IMockict, ISpring, IDatabase {
    static ISpecExecutorFactory                  specFactory   = ICoreInitial.initSpecExecutorFactory();

    private final Map<SpecMethodID, ISpecMethod> specMethods;

    private Map<String, Steps>                   stepsInstances;

    private static ThreadLocal<ISpecPrinter>     threadPrinter = new ThreadLocal<ISpecPrinter>();

    public JSpec() {
        this.specMethods = specFactory.findMethodsInSpec(this.getClass());
    }

    @Override
    public Steps getStepsInstance(String stepClazzName) {
        return this.stepsInstances.get(stepClazzName);
    }

    /**
     * 用来初始化共享的数据结构<br>
     * 允许子类进行覆盖
     */
    protected void initSharedData() {
    }

    @Override
    public SharedData getSharedData() {
        return new SharedData.EmptyData();
    }

    public abstract void runScenario(IScenario scenario) throws Throwable;

    @BeforeClass(alwaysRun = true)
    public void initPrinter() {
    }

    @AfterClass(alwaysRun = true)
    public static void cleanSpecPrinter() {
        getPrinter().printSummary(TestContext.currTestedClazz());
        threadPrinter.remove();
    }

    @DataProvider
    public DataProviderIterator<IScenario> story() {
        DataProviderIterator<IScenario> it = specFactory.findScenario(this.getClass());
        return it;
    }

    public void run(IScenario scenario) throws Throwable {
        this.initSharedData();
        this.stepsInstances = specFactory.newSteps(this);
        specFactory.runScenario(this, scenario, specMethods, getPrinter());
    }

    static ISpecPrinter getPrinter() {
        ISpecPrinter printer = threadPrinter.get();
        if (printer == null) {
            printer = specFactory.newSpecPrinter();
            threadPrinter.set(printer);
        }
        return printer;
    }
}
