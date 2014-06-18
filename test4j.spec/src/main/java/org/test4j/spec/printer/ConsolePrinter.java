package org.test4j.spec.printer;

import org.test4j.spec.ISpec;
import org.test4j.spec.exceptions.SkipStepException;
import org.test4j.spec.inner.IScenario;
import org.test4j.spec.inner.IScenarioStep;

/**
 * 将测试场景运行结果打印到控制台上
 * 
 * @author darui.wudr
 */
public class ConsolePrinter extends AbstractPrinter {
    String title;

    @Override
    public void printSummary(Class<? extends ISpec> spec) {
    }

    @Override
    protected void printHeader(ISpec spec, IScenario scenario) {
        System.out.print("\n\n");
        StringBuffer buff = new StringBuffer();
        buff.append("Scenario-").append(String.format("%02d", scenario.getIndex())).append(": ")
                .append(scenario.getName());
        this.title = buff.toString();
        System.out.println("====================" + "Begin " + title + "====================");
    }

    protected void printTailer(ISpec spec, IScenario scenario) {
        System.out.println("====================" + "End " + title + "====================");
    }

    @Override
    protected void printStep(IScenarioStep step) {
        StringBuffer buff = new StringBuffer("* ");
        if (step.isSuspend()) {
            buff.append("SUSPEND ");
        } else if (step.isSuccess()) {
            buff.append("SUCCESS ");
        } else {
            buff.append("FAILURE ");
        }
        buff.append(step.getType().name()).append("\t: ").append(step.getMethod());
        if (step.isSuccess() == false) {
            Throwable e = step.getError();
            if (e != null && !(e instanceof SkipStepException)) {
                buff.append("\n\t").append(e.getMessage());
            }
            System.err.println(buff.toString());
        } else {
            System.out.println(buff.toString());
        }
    }
}
