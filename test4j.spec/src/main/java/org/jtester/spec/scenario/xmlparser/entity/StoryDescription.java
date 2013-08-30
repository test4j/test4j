package org.jtester.spec.scenario.xmlparser.entity;

import org.dom4j.Document;
import org.dom4j.Element;
import org.jtester.spec.scenario.xmlparser.StoryFeature;
import org.jtester.spec.scenario.xmlparser.StoryNodeEntity;

/**
 * story xml描述节点
 * 
 * @author darui.wudr 2012-7-12 下午2:03:31
 */
@SuppressWarnings("serial")
public class StoryDescription implements StoryNodeEntity {
	private String description;

	private String displayText;

	public StoryDescription() {
	}

	public StoryDescription(String description) {
		this.description = description;
		this.displayText = StoryFeature.convetTextToHTML(description);
	}

	public String getDescription() {
		return description;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDescription(String description) {
		this.description = description;
		this.displayText = StoryFeature.convetTextToHTML(description);
	}

	public String getEntityName() {
		return "用例描述";
	}

	static final String pathID = PathID_Story_Description;

	public String getPathID() {
		return pathID;
	}

	public StoryNodeEntity setDescription(Document document) {
		Element node = (Element) document.selectSingleNode(pathID);
		this.description = node == null ? "" : node.getText();
		return this;
	}
}
