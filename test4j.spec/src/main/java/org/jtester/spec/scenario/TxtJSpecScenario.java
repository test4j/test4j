package org.jtester.spec.scenario;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jtester.spec.inner.IScenario;
import org.jtester.spec.inner.IScenarioStep;
import org.jtester.spec.reader.LinesReader;
import org.jtester.spec.reader.StreamLinesReader;
import org.jtester.spec.reader.StringLinesReader;
import org.jtester.spec.scenario.step.JSpecStep;
import org.jtester.spec.scenario.step.TxtJSpecStep;
import org.jtester.spec.scenario.step.TxtJSpecStep.TxtJSpecStepTemplate;
import org.jtester.spec.scenario.step.txt.LineType;
import org.jtester.spec.scenario.xmlparser.TxtStoryFeature;

/**
 * 文本文件描述的故事场景解析器
 * 
 * @author darui.wudr 2012-6-28 下午3:26:22
 */
public class TxtJSpecScenario extends JSpecScenario {

    public TxtJSpecScenario(List<String> lines, List<IScenarioStep> templates) {
        this.parse(lines, templates);
    }

    private void parse(List<String> lines, List<IScenarioStep> templates) {
        StringBuilder buff = new StringBuilder();
        JSpecStep step = null;
        IScenarioStep template = null;
        for (String line : lines) {
            LineType type = LineType.getLineType(line);
            switch (type) {
                case Scenario:
                    this.scenario = line.substring(8).trim();
                    this.isSkip = false;
                    break;
                case SkipScenario:
                    this.scenario = line.substring(12).trim();
                    this.isSkip = true;
                    break;
                // all kinds of GWZ line
                case Given:
                case SkipGiven:
                case When:
                case SkipWhen:
                case Then:
                case SkipThen:
                    addStep(buff, step, template);
                    step = new TxtJSpecStep(this.scenario, line, type);
                    template = JSpecStep.findTemplate(templates, step.getMethod(), step.getType());
                    buff = new StringBuilder();
                    break;
                case TextLine:
                default:
                    if (buff.length() != 0) {
                        buff.append("\n");
                    }
                    buff.append(line);
                    break;
            }
        }
        this.addStep(buff, step, template);
    }

    /**
     * 往列表中增加步骤
     * 
     * @param buff
     * @param step
     */
    private void addStep(StringBuilder buff, JSpecStep step, IScenarioStep template) {
        if (step == null) {
            this.description = buff.toString();
        } else {
            step.parseStep(buff.toString(), template);
            this.steps.add(step);
        }
    }

    static List<IScenario> parseJSpecScenarioFrom(LinesReader reader) {
        TxtStoryFeature storyFeature = parseTxtStoryFeatureFrom(reader);
        return storyFeature.getScenarios();
    }

    public static TxtStoryFeature parseTxtStoryFeatureFrom(LinesReader reader) {
        TxtStoryFeature storyFeature = new TxtStoryFeature();
        try {
            String line = parseStepTemplates(reader, storyFeature);
            // 如果已经读完文本，直接返回
            if (line == null) {
                return storyFeature;
            }
            // 依次读取每个场景的文本描述
            List<String> lines = new ArrayList<String>();
            lines.add(line);
            do {
                line = reader.readLine();
                if (line == null || LineType.isScenarioLine(line)) {
                    IScenario scenario = new TxtJSpecScenario(lines, storyFeature.getTemplates());
                    storyFeature.getScenarios().add(scenario);
                    if (line != null) {
                        lines = new ArrayList<String>();
                        lines.add(line);
                    }
                } else {
                    lines.add(line);
                }
            } while (line != null);
            return storyFeature;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            reader.close();
        }
    }

    /**
     * 解析模板方法列表
     * 
     * @param reader
     * @param templates
     * @return
     * @throws IOException
     */
    static String parseStepTemplates(LinesReader reader, TxtStoryFeature storyFeature) throws IOException {
        String line = reader.readLine();
        // 跳过第一个场景或模板之前的描述文字
        StringBuffer description = new StringBuffer();
        while (line != null && !LineType.isScenarioLine(line) && !LineType.isTemplateLine(line)) {
            description.append(line);
            line = reader.readLine();
            continue;
        }
        storyFeature.setDescription(description.toString());

        List<String> lines = null;
        while (line != null && !LineType.isScenarioLine(line)) {
            if (LineType.isTemplateLine(line) || lines == null) {
                addTemplate(storyFeature.getTemplates(), lines);
                lines = new ArrayList<String>();
                lines.add(line);
            } else {
                lines.add(line);
            }
            line = reader.readLine();
        }
        addTemplate(storyFeature.getTemplates(), lines);
        return line;
    }

    /**
     * 增加模板列表
     * 
     * @param templates
     * @param lines
     */
    private static void addTemplate(List<IScenarioStep> templates, List<String> lines) {
        if (lines == null || lines.size() == 0) {
            return;
        }
        String capital = null;
        StringBuilder buff = new StringBuilder();
        for (String line : lines) {
            if (capital == null) {
                capital = line;
            } else {
                buff.append(line);
            }
        }
        JSpecStep template = new TxtJSpecStepTemplate(capital, LineType.getLineType(capital));
        template.parseStep(buff.toString(), null);
        templates.add(template);
    }

    public static List<IScenario> parseJSpecScenarioFrom(String story) {
        String lines[] = story.split("\n");
        LinesReader reader = new StringLinesReader(lines);
        List<IScenario> scenarios = parseJSpecScenarioFrom(reader);
        return scenarios;
    }

    /***
     * 从文本流中解析需要运行的测试场景
     * 
     * @param is
     * @param encoding 文本流编码，如果为null，则自动获取，如果自动获取失败，则使用默认编码
     * @return
     */
    public static List<IScenario> parseJSpecScenarioFrom(InputStream is, String encoding) {
        LinesReader reader = new StreamLinesReader(is, encoding);
        List<IScenario> scenarios = parseJSpecScenarioFrom(reader);
        return scenarios;
    }
}
