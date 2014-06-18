package org.test4j.spec.scenario.step.txt;

import org.test4j.spec.inner.StepType;
import org.test4j.spec.scenario.step.txt.LineType;

public enum LineType {
    /**
     * Given 关键字行
     */
    Given {
        @Override
        public StepType getStepType() {
            return StepType.Given;
        }
    },
    /**
     * When 关键字行
     */
    When {
        @Override
        public StepType getStepType() {
            return StepType.When;
        }
    },
    /**
     * Then关键字行
     */
    Then {
        @Override
        public StepType getStepType() {
            return StepType.Then;
        }
    },
    /**
     * SkipGiven 关键字行
     */
    SkipGiven {
        @Override
        public StepType getStepType() {
            return StepType.Given;
        }
    },
    /**
     * SkipWhen 关键字行
     */
    SkipWhen {
        @Override
        public StepType getStepType() {
            return StepType.When;
        }
    },
    /**
     * SkipThen关键字行
     */
    SkipThen {
        @Override
        public StepType getStepType() {
            return StepType.Then;
        }
    },
    /**
     * GivenTemplate关键字行
     */
    GivenTemplate {
        @Override
        public StepType getStepType() {
            return StepType.Given;
        }
    },
    /**
     * WhenTemplate关键字行
     */
    WhenTemplate {
        @Override
        public StepType getStepType() {
            return StepType.When;
        }
    },
    /**
     * ThenTemplate关键字行
     */
    ThenTemplate {
        @Override
        public StepType getStepType() {
            return StepType.Then;
        }
    },
    /**
     * Story 关键字行
     */
    Story,
    /**
     * 场景前执行步骤
     */
    BeforeScenario,
    /**
     * Scenario 关键字行
     */
    Scenario,
    /**
     * 场景后执行步骤
     */
    AfterScenario,
    /**
     * SkipScenario关键字行
     */
    SkipScenario,
    /**
     * 普通文本行
     */
    TextLine {
        @Override
        public boolean match(String line) {
            for (LineType type : LineType.values()) {
                if (type == TextLine) {
                    continue;
                }
                boolean match = type.match(line);
                if (match) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public String getSurfixText(String line) {
            return line;
        }
    };

    public static LineType getLineType(String line) {
        if (line == null) {
            return TextLine;
        }
        String text = line.trim();
        for (LineType type : LineType.values()) {
            if (type != TextLine && type.match(text)) {
                return type;
            }
        }
        return TextLine;
    }

    /**
     * 返回对应的StepType类型
     * 
     * @return
     */
    public StepType getStepType() {
        return null;
    }

    public boolean match(String line) {
        if (line.equals(this.name())) {
            return true;
        } else if (line.startsWith(this.name() + " ")) {
            return true;
        } else if (line.startsWith(this.name() + "\t")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isScenarioLine(String line) {
        if (line == null) {
            return false;
        }
        String text = line.trim();

        if (Scenario.match(text) || SkipScenario.match(text) || BeforeScenario.match(text) || AfterScenario.match(text)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否是模板方法定义行
     * 
     * @param line
     * @return
     */
    public static boolean isTemplateLine(String line) {
        if (line == null) {
            return false;
        }
        String text = line.trim();
        if (GivenTemplate.match(text) || WhenTemplate.match(text) || ThenTemplate.match(text)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 返回去掉关键字后的文本
     * 
     * @param line
     * @return
     */
    public String getSurfixText(String line) {
        String text = line == null ? "" : line.trim();
        int index = this.name().length();
        if (text.length() > index) {
            return text.substring(index).trim();
        } else {
            return "";
        }
    }

    public static LineType scenarioType(String line) {
        if (Scenario.match(line)) {
            return Scenario;
        } else if (SkipScenario.match(line)) {
            return SkipScenario;
        } else if (BeforeScenario.match(line)) {
            return BeforeScenario;
        } else if (AfterScenario.match(line)) {
            return AfterScenario;
        } else {
            return null;
        }
    };
}
