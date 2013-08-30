package org.jtester.spec.scenario.xmlparser.entity;

import java.util.ArrayList;
import java.util.List;

import org.jtester.spec.scenario.step.JSpecStep;
import org.jtester.spec.scenario.xmlparser.StoryNodeEntity;

@SuppressWarnings("serial")
public class TemplateList implements StoryNodeEntity {
	private List<TemplateMethod> methods;

	public TemplateList() {
		this.methods = new ArrayList<TemplateMethod>();
	}

	public String getEntityName() {
		return "模板列表";
	}

	public List<TemplateMethod> getMethods() {
		return methods;
	}

	public void setMethods(List<TemplateMethod> methods) {
		this.methods = methods;
	}

	/**
	 * 增加步骤模板
	 * 
	 * @param method
	 * @return
	 */
	public TemplateList add(TemplateMethod method) {
		this.methods.add(method);
		return this;
	}

	public String getPathID() {
		return PathID_Story_TemplateList;
	}

	/**
	 * 根据名称和类型查找模板
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public TemplateMethod findTemplate(String name, String type) {
		if (name == null || type == null) {
			return null;
		}
		for (TemplateMethod method : methods) {
			if (name.equals(method.getMethodName()) && type.equals(method.getMethodType())) {
				return method;
			}
		}
		return null;
	}

	public List<JSpecStep> getTemplates() {
		List<JSpecStep> templates = null;// TODO
		return templates;
	}
}
