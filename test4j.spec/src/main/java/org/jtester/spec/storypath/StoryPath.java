package org.jtester.spec.storypath;

import java.util.List;

import org.jtester.spec.ISpec;
import org.jtester.spec.annotations.StoryFile;
import org.jtester.spec.annotations.StorySource;
import org.jtester.spec.annotations.StoryType;
import org.jtester.spec.inner.IScenario;
import org.jtester.spec.reader.TitianHttpRequest;
import org.jtester.tools.commons.ConfigHelper;

public abstract class StoryPath {
    /**
     * 文件内容格式,xml或者txt
     */
    public static String STORY_TYPE       = "jspec.file.type";
    /**
     * jspec运行的数据来源，classpath: 本地classpath路径；titian: 梯田远程提供
     */
    public static String STORY_SOURCE     = "jspec.story.source";
    /**
     * 梯田服务地址
     */
    public static String TITIAN_URL       = "jspec.titian.url";
    /**
     * 默认站点
     */
    public static String TITIAN_SITE      = "jspec.titian.site";
    /**
     * 默认项目编号
     */
    public static String TITIAN_PROJECTID = "jspec.titian.project";

    /**
     * 按照指定的编码解析用例场景
     * 
     * @param encoding 如果没有指定编码，则按照默认方式解析
     * @return
     */
    public abstract List<IScenario> getStory(StoryFile storyFile, String encoding);

    /**
     * 根据jtester配置文件构造对应的用例文件解析器
     * 
     * @return
     */
    public static StoryPath factory(Class<? extends ISpec> claz, StoryFile storyFile) {
        StorySource source = StorySource.Titian;
        if (storyFile == null || storyFile.source() == StorySource.Default) {
            String prop = ConfigHelper.getString(STORY_SOURCE);
            if ("TITIAN".equalsIgnoreCase(prop)) {
                source = StorySource.Titian;
            } else {
                source = StorySource.ClassPath;
            }
        } else {
            source = storyFile.source();
        }
        if (source == StorySource.Titian) {
            return titian(claz);
        } else {
            return classpath(claz);
        }
    }

    /**
     * 获取story文件类型
     * 
     * @param story
     * @return
     */
    protected StoryType getStoryType(StoryFile story) {
        StoryType type = story == null ? StoryType.DEFAULT : story.type();
        if (type != StoryType.DEFAULT) {
            return type;
        }
        String _type = ConfigHelper.getString(STORY_TYPE, "xml");
        if (_type.equalsIgnoreCase("txt")) {
            return StoryType.TXT;
        } else {
            return StoryType.XML;
        }
    }

    private static TitianHttpRequest titianRequest = null;

    public static StoryPath titian(Class<? extends ISpec> claz) {
        if (titianRequest == null) {
            String baseURL = ConfigHelper.getString(TITIAN_URL);// "http://localhost:8080/titian/";
            String site = ConfigHelper.getString(TITIAN_SITE);// "CRM";
            String projectID = ConfigHelper.getString(TITIAN_PROJECTID);// "1";
            titianRequest = new TitianHttpRequest(baseURL, site, projectID);
        }
        return new TitianStoryPath(titianRequest, claz);
    }

    public static StoryPath classpath(Class<? extends ISpec> claz) {
        return new ClassPathStoryPath(claz);
    }
}
