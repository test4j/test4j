package org.jtester.spec.printer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jtester.spec.ISpec;
import org.jtester.spec.inner.IScenario;
import org.jtester.spec.inner.IScenarioStep;
import org.jtester.tools.commons.ResourceHelper;

public class HtmlFilePrinter extends AbstractPrinter {
    static String outputDir = System.getProperty("user.dir") + "/jspec-output/";

    static String Default_Html_Style;
    static {
        try {
            Default_Html_Style = ResourceHelper.readFromFile(HtmlFilePrinter.class, "printer.css");
        } catch (Exception e) {
            Default_Html_Style = "";
            e.printStackTrace();
        }
    }

    List<String>  htmls     = new ArrayList<String>(20);

    @Override
    public void printSummary(Class<? extends ISpec> spec) {
        StringBuilder summary = new StringBuilder();
        summary.append("<html><head>");
        summary.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
        summary.append("<style>").append(Default_Html_Style).append("</style>");
        summary.append("</head><body><div>");
        for (String html : htmls) {
            summary.append(html);
        }
        summary.append("</div></body></html>");
        ResourceHelper.writeStringToFile(new File(outputDir + getSpecHtmlFile(spec)), summary.toString());
        htmls.clear();
    }

    String getSpecHtmlFile(Class<? extends ISpec> spec) {
        String file = spec.getName().replace('.', '/');
        return file + ".html";
    }

    StringBuilder scenarioHtml = null;

    @Override
    protected void printHeader(ISpec spec, IScenario scenario) {
        scenarioHtml = new StringBuilder();
        scenarioHtml.append("<table>");
        scenarioHtml.append("<tr><th colspan=\"2\">").append(scenario.getName()).append("</th></tr>");
    }

    @Override
    protected void printStep(IScenarioStep step) {
        scenarioHtml.append("<tr>");
        String claz = this.getStyle(step);
        if (step.isFailure()) {
            scenarioHtml.append(String.format("<td rowspan=\"2\" class=\"%s\">", claz));
        } else {
            scenarioHtml.append(String.format("<td class=\"%s\">", claz));
        }
        scenarioHtml.append(step.getMethod()).append("</td>");
        scenarioHtml.append("<td>").append(step.getDisplayText());

        scenarioHtml.append("</td>");
        scenarioHtml.append("</tr>");
        if (step.isFailure()) {
            // String error = StringHelper.toString(step.getError());
            String error = step.getError().getMessage();
            error = error == null ? "" : error;
            scenarioHtml.append("<tr><td>").append(error.replaceAll("\\n", "<br/>")).append("</td></tr>");
        }
    }

    @Override
    protected void printTailer(ISpec spec, IScenario scenario) {
        scenarioHtml.append("</table>");
        htmls.add(this.scenarioHtml.toString());
        scenarioHtml = null;
    }

    private String getStyle(IScenarioStep step) {
        if (step.isSuspend()) {
            return "suspend";
        } else if (step.isSuccess()) {
            return "success";
        } else {
            return "failure";
        }
    }
}
