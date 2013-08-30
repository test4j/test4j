package org.jtester.spec.printer;

import org.jtester.spec.ISpec;
import org.jtester.spec.inner.IScenario;
import org.jtester.spec.inner.IScenarioStep;
import org.jtester.spec.inner.ISpecPrinter;

/**
 * @author darui.wudr 2013-1-22 上午9:43:22
 */
public abstract class AbstractPrinter implements ISpecPrinter {

    @Override
    public final void printScenario(ISpec spec, IScenario scenario) {
        this.printHeader(spec, scenario);
        for (IScenarioStep step : scenario.getSteps()) {
            this.printStep(step);
        }
        this.printTailer(spec, scenario);
    }

    /**
     * 输出场景开始信息
     * 
     * @param scenario
     */
    protected abstract void printHeader(ISpec spec, IScenario scenario);

    /**
     * 输出场景结束信息
     * 
     * @param scenario
     */
    protected abstract void printTailer(ISpec spec, IScenario scenario);

    /**
     * 输出场景步骤信息
     * 
     * @param step
     */
    protected abstract void printStep(IScenarioStep step);
}
