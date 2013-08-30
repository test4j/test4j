package org.jtester.json.encoder;

import org.jtester.json.JSON;
import org.jtester.json.encoder.beans.test.User;
import org.jtester.json.encoder.object.PoJoEncoder;
import org.jtester.json.helper.JSONFeature;
import org.testng.annotations.Test;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Test(groups = { "jtester", "json" })
public class PropertyEncoderTest extends EncoderTest {
	String json = "";
	User user = new User() {
		{
			this.setId(123);
			this.setName("darui.wu");
			this.setAge(45);
			this.setSalary(234.56d);
		}
	};

	@Test
	public void testEncodeValue() {

		JSONEncoder encoder = new PoJoEncoder(User.class);
		encoder.setFeatures(JSONFeature.UseSingleQuote, JSONFeature.IgnoreExplicitFieldType);

		encoder.encode(user, writer, references);
		this.json = writer.toString();
		want.string(json).start("{#class:'org.jtester.json.encoder.beans.test.User@").contains("id:123")
				.contains("name:'darui.wu'").contains("age:45").contains("salary:234.56").contains("isFemale:false");
	}

	@Test(dependsOnMethods = "testEncodeValue")
	public void testDecodeValue() {
		User result = JSON.toObject(json);
		want.object(user).reflectionEq(result);
	}
}
