package org.test4j.json.encoder;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.test4j.json.encoder.JSONEncoder;
import org.test4j.json.helper.JSONFeature;
import org.test4j.junit.Test4J;

@SuppressWarnings("rawtypes")
public abstract class EncoderTest implements Test4J {
    protected StringWriter writer     = null;
    protected List<String> references = null;

    @Before
    public void initStringWriter() {
        writer = new StringWriter();
        this.references = new ArrayList<String>();
    }

    /**
     * 设置编码器输出json串不带class标记
     * 
     * @param encoder
     */
    protected void setUnmarkFeature(JSONEncoder encoder) {
        encoder.setFeatures(JSONFeature.UseSingleQuote, JSONFeature.UnMarkClassFlag);
    }
}
