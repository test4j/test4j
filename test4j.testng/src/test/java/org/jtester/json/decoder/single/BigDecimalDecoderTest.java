package org.jtester.json.decoder.single;

import java.math.BigDecimal;
import java.util.HashMap;

import org.jtester.json.JSON;
import org.jtester.json.decoder.single.BigDecimalDecoder;
import org.jtester.json.helper.JSONFeature;
import org.jtester.json.helper.JSONMap;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "json" })
public class BigDecimalDecoderTest extends JTester {

	@SuppressWarnings("serial")
	@Test
	public void testDecodeSimpleValue() {
		JSONMap json = new JSONMap() {
			{
				this.putJSON(JSONFeature.ValueFlag, "1213435");
			}
		};
		BigDecimalDecoder decoder = BigDecimalDecoder.toBIGDECIMAL;
		BigDecimal decimal = decoder.decode(json, BigDecimal.class, new HashMap<String, Object>());
		want.number(decimal).isEqualTo(new BigDecimal("1213435"));
	}

	public void testSimpleValue() {
		BigDecimal decimal = JSON.toObject("1213435", BigDecimal.class);
		want.number(decimal).isEqualTo(new BigDecimal("1213435"));
	}
}
