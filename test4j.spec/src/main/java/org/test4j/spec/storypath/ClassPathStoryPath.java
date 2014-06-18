package org.test4j.spec.storypath;

import java.io.InputStream;

import org.test4j.spec.ISpec;
import org.test4j.spec.annotations.StoryFile;
import org.test4j.spec.annotations.StoryType;
import org.test4j.spec.scenario.JSpecScenario;
import org.test4j.spec.scenario.Story;
import org.test4j.tools.commons.StringHelper;

/**
 * 从本地classpath读取jspec用例描述信息
 * 
 * @author darui.wudr 2012-5-18 上午10:10:55
 */
public class ClassPathStoryPath extends StoryPath {
    static final String DOT_REGEX         = "\\.";
    static final String SLASH             = "/";
    static final String EMPTY             = "";
    static final String DEFAULT_EXTENSION = ".story";

    private String      path;
    private String      name;
    private ClassLoader classLoader;

    public ClassPathStoryPath(Class<? extends ISpec> claz) {
        this.path = this.resolveDirectory(claz);
        this.name = this.resolveName(claz);
        this.classLoader = claz.getClassLoader();
    }

    String getStoryFile(StoryType type, StoryFile story) {
        String file = story == null ? "" : story.value().trim();
        if (StringHelper.isEmpty(file)) {
            StringBuffer sb = new StringBuffer();
            if (path.length() > 0) {
                sb.append(path).append(SLASH);
            }
            sb.append(name);
            sb.append(StoryType.XML.equals(type) ? ".xml" : DEFAULT_EXTENSION);
            return sb.toString();
        } else if (file.startsWith("classpath:")) {
            return file.substring(10);
        } else {
            StringBuffer sb = new StringBuffer();
            if (path.length() > 0) {
                sb.append(path).append(SLASH);
            }
            sb.append(file);
            return sb.toString();
        }
    }

    protected String resolveDirectory(Class<? extends ISpec> claz) {
        Package pack = claz.getPackage();
        if (pack != null) {
            return pack.getName().replaceAll(DOT_REGEX, SLASH);
        }
        return EMPTY;
    }

    @Override
    public Story getStory(StoryFile storyFile, String encoding) {
        StoryType type = getStoryType(storyFile);
        String storyPath = this.getStoryFile(type, storyFile);
        InputStream stream = classLoader.getResourceAsStream(storyPath);
        if (stream == null) {
            throw new RuntimeException("Story path '" + storyPath + "' not found by class loader " + classLoader);
        }
        Story story = JSpecScenario.parseFrom(type, stream, encoding);
        this.clean();
        return story;
    }

    protected String resolveName(Class<? extends ISpec> claz) {
        return claz.getSimpleName();
    }

    private void clean() {
        this.classLoader = null;
        this.path = null;
        this.name = null;
    }
}
