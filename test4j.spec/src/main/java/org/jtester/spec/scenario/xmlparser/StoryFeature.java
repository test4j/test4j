package org.jtester.spec.scenario.xmlparser;

import java.io.Serializable;

import org.jtester.spec.scenario.xmlparser.entity.ScenarioList;
import org.jtester.spec.scenario.xmlparser.entity.StoryDescription;
import org.jtester.spec.scenario.xmlparser.entity.TemplateList;

/**
 * 一个完整的用例对象
 * 
 * @author darui.wudr 2012-7-16 下午2:00:53
 */
@SuppressWarnings("serial")
public class StoryFeature implements Serializable {
	private StoryDescription description;

	private TemplateList templates;

	private ScenarioList scenarios;

	public StoryDescription getDescription() {
		return description;
	}

	public void setDescription(StoryDescription description) {
		this.description = description;
	}

	public TemplateList getTemplates() {
		return templates;
	}

	public void setTemplates(TemplateList templates) {
		this.templates = templates;
	}

	public ScenarioList getScenarios() {
		return scenarios;
	}

	public void setScenarios(ScenarioList scenarios) {
		this.scenarios = scenarios;
	}

	public static String convetTextToHTML(String text) {
		String html = text.replaceAll("\\n", "<br/>").replaceAll("\\s", "&nbsp;");
		return html;
	}
}
