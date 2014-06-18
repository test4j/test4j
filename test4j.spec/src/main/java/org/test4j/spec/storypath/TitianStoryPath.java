package org.test4j.spec.storypath;

import java.net.URLDecoder;

import org.test4j.spec.ISpec;
import org.test4j.spec.annotations.StoryFile;
import org.test4j.spec.annotations.StoryType;
import org.test4j.spec.reader.TitianHttpRequest;
import org.test4j.spec.scenario.JSpecScenario;
import org.test4j.spec.scenario.Story;
import org.test4j.tools.commons.ExceptionWrapper;

/**
 * 远程从梯田系统获取用例描述信息
 * 
 * @author darui.wudr 2012-5-18 上午10:33:32
 */
public class TitianStoryPath extends StoryPath {
    private TitianHttpRequest titianRequest = null;

    private final String      clazz;

    public TitianStoryPath(TitianHttpRequest titianRequest, Class<? extends ISpec> claz) {
        this.titianRequest = titianRequest;
        this.clazz = claz.getName();
    }

    @Override
    public Story getStory(StoryFile storyFile, String encoding) {
        try {
            String feature = getFeature();
            StoryType type = getStoryType(storyFile);
            Story story = JSpecScenario.parseFrom(type, feature);
            return story;
        } catch (Exception e) {
            ExceptionWrapper.throwRuntimeException(e);
            return null;
        }
    }

    private String getFeature() throws Exception {
        String result = titianRequest.sendGetRequest(this.clazz);
        if (result == null) {
            throw new RuntimeException("null content return from url[" + titianRequest.titianURL(clazz) + "].");
        } else if (result.startsWith("success=")) {
            String feature = URLDecoder.decode(result.substring(8), "UTF-8").trim();
            return feature;
        } else if (result.startsWith("failure=")) {
            String error = URLDecoder.decode(result.substring(8), "UTF-8").trim();
            throw new RuntimeException(error);
        } else {
            throw new RuntimeException("illegal content return from url[" + titianRequest.titianURL(clazz) + "]:"
                    + result);
        }
    }
}
