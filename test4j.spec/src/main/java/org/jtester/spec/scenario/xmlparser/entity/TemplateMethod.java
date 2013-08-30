package org.jtester.spec.scenario.xmlparser.entity;

import java.util.LinkedHashMap;

import org.jtester.spec.inner.StepType;
import org.jtester.spec.scenario.xmlparser.StoryNodeEntity;

/**
 * 模板方法定义
 * 
 * @author darui.wudr 2012-7-12 下午2:24:51
 */
@SuppressWarnings("serial")
public class TemplateMethod implements StoryNodeEntity {
	private String name;

	private StepType type;
	/**
	 * 模板参数
	 */
	private LinkedHashMap<String, String> paras;

	/**
	 * 原始内容
	 */
	private String initialText;
	/**
	 * 模板内容
	 */
	private String templateText;
	/**
	 * 显示内容，有模板内容替换参数得到
	 */
	private String displayText;

	public TemplateMethod(String templateName, StepType type) {
		this.name = templateName;
		this.type = type;
	}

	public TemplateMethod(String templateName, String type) {
		this.name = templateName;
		this.type = StepType.getStepType(type);
	}

	public String getMethodName() {
		return this.name;
	}

	/**
	 * 返回方法类型（字符串表示）
	 * 
	 * @return
	 */
	public String getMethodType() {
		return type == null ? "" : type.name().toLowerCase();
	}

	public String getEntityName() {
		return this.getMethodName();
	}

	private int templateIndex;

	public String getPathID() {
		return String.format(PathID_Story_Template_Method, templateIndex);
	}

	/**
	 * 设置模板在document中的序号
	 * 
	 * @param templateIndex
	 */
	public TemplateMethod setPathID(int templateIndex) {
		this.templateIndex = templateIndex;
		return this;
	}

	public LinkedHashMap<String, String> getParas() {
		return this.paras;
	}

	public TemplateMethod setParas(LinkedHashMap<String, String> paras) {
		this.paras = paras;
		return this;
	}

	public String getInitialText() {
		return initialText;
	}

	public TemplateMethod setInitialText(String initialText) {
		this.initialText = initialText;
		return this;
	}

	public String getTemplateText() {
		return templateText;
	}

	public TemplateMethod setTemplateText(String templateText) {
		this.templateText = templateText;
		return this;
	}

	public String getDisplayText() {
		return displayText;
	}

	public TemplateMethod setDisplayText(String displayText) {
		this.displayText = displayText;
		return this;
	}
}
