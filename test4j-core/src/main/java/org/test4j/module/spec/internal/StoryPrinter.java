package org.test4j.module.spec.internal;

import lombok.Getter;
import org.test4j.tools.Logger;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.test4j.tools.commons.ResourceHelper.copyClassPathResource;
import static org.test4j.tools.commons.ResourceHelper.writeStringToFile;

/**
 * StoryPrinter: 输出测试结果
 *
 * @author darui.wu
 * @create 2019/11/12 9:11 上午
 */
public class StoryPrinter {
    private static ThreadLocal<List<ScenarioInfo>> CURR_SCENARIO_LIST = new ThreadLocal<List<ScenarioInfo>>();

    public static String getStoryPath() {
        return System.getProperty("user.dir") + "/target/story/";
    }

    public static void print(String scenarioPath, String scenarioName, String scenarioResult, Throwable testThrowable) {
        Logger.info(scenarioResult);

        String scenarioFile = scenarioPath + ".txt";
        try {
            writeStringToFile(new File(getStoryPath() + "scenario/" + scenarioFile), scenarioResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (CURR_SCENARIO_LIST.get() == null) {
            CURR_SCENARIO_LIST.set(new ArrayList<>());
        }
        CURR_SCENARIO_LIST.get().add(new ScenarioInfo(scenarioName, scenarioFile, testThrowable == null));
    }

    private static File indexFile;

    public static void printScenarioIndex(String testKlassName) {
        initIndexFile();
        List<ScenarioInfo> scenarios = CURR_SCENARIO_LIST.get();
        if (scenarios == null) {
            return;
        }
        try (FileWriter writer = new FileWriter(indexFile, true)) {
            writer.write("<li>\n");
            writer.write(String.format("<span class='badge %s'>%s</span>\n", ScenarioInfo.listStyle(scenarios), testKlassName));
            writer.write("<ul>\n");
            String context = scenarios.stream().map(StoryPrinter::printScenarioIndex)
                .collect(Collectors.joining("\n"));
            writer.write(context);
            writer.write("</ul>\n");
            writer.write("</li>\n");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CURR_SCENARIO_LIST.get().clear();
        }
    }

    private static String printScenarioIndex(ScenarioInfo scenario) {
        return new StringBuilder()
            .append("<li>")
            .append(String.format("<a class='badge %s' href='javascript:doShow(\"%s\");'>", scenario.getStyle(), scenario.getScenarioPath()))
            .append(scenario.getScenarioName())
            .append("</a>")
            .append("</li>")
            .toString();
    }

    private static void initIndexFile() {
        if (indexFile != null) {
            return;
        }
        String scPath = getStoryPath() + "scenario/";
        try {
            indexFile = new File(getStoryPath() + "index.html");
            writeStringToFile(new File(getStoryPath() + "scenario/ok.html"), "");
            copyClassPathResource("scenario/jquery-3.4.1.min.js", scPath + "jquery-3.4.1.min.js");
            copyClassPathResource("scenario/story.js", scPath + "story.js");
            copyClassPathResource("scenario/story.css", scPath + "story.css");
            copyClassPathResource("scenario/bootstrap.min.css", scPath + "bootstrap.min.css");
            copyClassPathResource("scenario/index.html", getStoryPath() + "index.html");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Getter
    public static class ScenarioInfo {
        String scenarioName;

        String scenarioPath;

        boolean isSuccess;

        public ScenarioInfo(String name, String path, boolean success) {
            this.scenarioName = name;
            this.scenarioPath = path;
            this.isSuccess = success;
        }


        public String getStyle() {
            return isSuccess ? "badge-success" : "badge-important";
        }

        public static String listStyle(List<ScenarioInfo> scenarios) {
            boolean hasSuccess = false;
            boolean hasFailure = false;
            for (ScenarioInfo scenario : scenarios) {
                if (scenario.isSuccess) {
                    hasSuccess = true;
                } else {
                    hasFailure = true;
                }
            }
            if (hasSuccess && hasFailure) {
                return "badge-warning";
            } else if (hasSuccess) {
                return "badge-success";
            } else {
                return "badge-important";
            }
        }
    }
}