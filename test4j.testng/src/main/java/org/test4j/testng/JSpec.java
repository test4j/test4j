package org.test4j.testng;

import java.util.Map;

import org.test4j.module.ICore;
import org.test4j.module.core.ICoreInitial;
import org.test4j.module.core.TestContext;
import org.test4j.module.database.IDatabase;
import org.test4j.module.jmockit.IMockict;
import org.test4j.module.spring.ISpring;
import org.test4j.spec.ISpec;
import org.test4j.spec.ISpecExecutorFactory;
import org.test4j.spec.SharedData;
import org.test4j.spec.inner.IScenario;
import org.test4j.spec.inner.ISpecMethod;
import org.test4j.spec.inner.ISpecMethod.SpecMethodID;
import org.test4j.spec.inner.ISpecPrinter;
import org.test4j.tools.datagen.DataProviderIterator;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

@SuppressWarnings({ "unchecked" })
public abstract class JSpec extends Test4JCore implements ISpec, ICore, IMockict, ISpring, IDatabase {
    static ISpecExecutorFactory                  specFactory   = ICoreInitial.initSpecExecutorFactory();

    private final Map<SpecMethodID, ISpecMethod> specMethods;

    private Map<String, Object>                  stepsInstances;

    private static ThreadLocal<ISpecPrinter>     threadPrinter = new ThreadLocal<ISpecPrinter>();

    public JSpec() {
        this.specMethods = specFactory.findMethodsInSpec(this.getClass());
    }

    @Override
    public Object getStepsInstance(String stepClazzName) {
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
