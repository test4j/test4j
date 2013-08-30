package org.jtester.spec.scenario.xmlparser;

/**
 * story xml文件各个节点名称，属性，xpath定义
 * 
 * @author darui.wudr 2012-7-12 下午2:00:22
 */
public interface StoryQName {
    /**
     * story文件根节点标签
     */
    String nodeStory               = "story";
    /**
     * story文件description节点
     */
    String nodeDescription         = "description";
    /**
     * story文件scenario description节点
     */
    String nodeScenarioDescription = "description";
    /**
     * story文件templates节点
     */
    String nodeMethodTemplate           = "template";
    /**
     * story文件template方法节点
     */
    String nodeTemplateMethod      = "method";
    //    /**
    //     * story文件场景列表节点
    //     */
    //    String nodeScenarios           = "scenarios";
    /**
     * story文件场景
     */
    String nodeScenario            = "scenario";
    /**
     * story文件场景方法
     */
    String nodeScenarioMethod      = "method";
    /**
     * 方法参数节点
     */
    String nodeMethodPara          = "para";

    /**
     * 根节点xpath表达式
     */
    String xpathRoot               = "/story";
    /**
     * 用例描述xpath表达式
     */
    String xpathDescription        = "/story/description";
    /**
     * story文件templates节点xpath表达式
     */
    String xpathTemplate           = "/story/template";
    /**
     * story文件templates方法节点xpath表达式
     */
    String xpathTemplateMethod     = "/story/template/method";
    //    /**
    //     * 场景列表xpath表达式
    //     */
    //    String xpathScenarios          = "/story/scenarios";
    /**
     * story文件场景xpath
     */
    String xpathScenario           = "/story/scenario";
    /**
     * story文件场景方法xpath
     */
    String xpathScenarioMethod     = "/story/scenario/method";

    /**
     * name attribute
     */
    String attrName                = "name";
    /**
     * type attribute
     */
    String attrType                = "type";
    /**
     * skip attribute
     */
    String attrSkip                = "skip";
}
