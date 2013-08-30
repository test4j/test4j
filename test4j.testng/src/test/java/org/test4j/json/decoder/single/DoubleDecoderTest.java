package org.test4j.json.decoder.single;

import java.util.HashMap;

import org.test4j.json.JSON;
import org.test4j.json.decoder.single.DoubleDecoder;
import org.test4j.json.helper.JSONMap;
import org.test4j.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "json" })
public class DoubleDecoderTest extends JTester {

	public void testDecodeSimpleValue() {
		JSONMap json = new JSONMap() {
			private static final long serialVersionUID = 1L;
			{
				this.putJSON("#value", 23243.34d);
			}
		};
		DoubleDecoder decoder = DoubleDecoder.toDOUBLE;
		Double d = decoder.decode(json, Double.class, new HashMap<String, Object>());
		want.number(d).isEqualTo(23243.34d);
	}

	@Test(dataProvider = "simple_value")
	public void testSimpleValue(String json, double expected) {
		Double actual = JSON.toObject(json, Double.class);
		want.number(actual).isEqualTo(expected);
	}

	@DataProvider
	public Object[][] simple_value() {
		return new Object[][] { { "34234.34d", 34234.34d },// <br>
				{ "'34234.34d'", 34234.34d }, /** <br> */
				{ "34234.34", 34234.34d } /** <br> */
		};
	}
}
