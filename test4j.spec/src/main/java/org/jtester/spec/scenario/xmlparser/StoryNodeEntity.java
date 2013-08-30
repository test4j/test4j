package org.jtester.spec.scenario.xmlparser;

import java.io.Serializable;

/**
 * xml文件节点基类
 * 
 * @author darui.wudr 2012-7-12 下午2:01:41
 */
public interface StoryNodeEntity extends Serializable {
	/**
	 * 返回节点名称
	 * 
	 * @return
	 */
	String getEntityName();

	/**
	 * 返回节点在xml中的位置信息用于定位
	 * 
	 * @return
	 */
	String getPathID();

	/**
	 * 用例描述的xpath表达式
	 */
	String PathID_Story_Description = "/story/description";
	/**
	 * 用例模板列表
	 */
	String PathID_Story_TemplateList = "/story/templates";
	/**
	 * 用例场景列表
	 */
	String PathID_Story_ScenarioList = "/story/scenarios";
	/**
	 * 用例单个场景的xpath表达式,需要传入参数
	 */
	String PathID_Story_Scenario = "/story/scenarios/scenario[%d]";
	/**
	 * 用例单个场景的描述,需要传入参数
	 */
	String PathID_Story_Scenario_Description = "/story/scenarios/scenario[%d]/description";
	/**
	 * 用例中某个方法的xpath表达式,需要传入参数
	 */
	String PathID_Story_Scenario_Method = "/story/scenarios/scenario[%d]/method[%d]";
	/**
	 * 某个模板方法的xpath表达式,需要传入参数
	 */
	String PathID_Story_Template_Method = "/story/templates/template[%d]";
}
