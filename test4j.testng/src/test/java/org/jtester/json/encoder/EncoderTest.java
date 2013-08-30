package org.jtester.json.encoder;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.jtester.json.helper.JSONFeature;
import org.jtester.testng.JTester;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("rawtypes")
@Test(groups = { "jtester", "json" })
public abstract class EncoderTest extends JTester {
	protected StringWriter writer = null;
	protected List<String> references = null;

	@BeforeMethod
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
