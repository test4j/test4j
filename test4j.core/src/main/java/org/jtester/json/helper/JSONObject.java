package org.jtester.json.helper;

import java.io.Serializable;

public interface JSONObject extends Serializable {

	final JSONSingle JSON_ClazzFlag = new JSONSingle(JSONFeature.ClazzFlag);

	final JSONSingle JSON_ValueFlag = new JSONSingle(JSONFeature.ValueFlag);

	final JSONSingle JSON_ReferFlag = new JSONSingle(JSONFeature.ReferFlag);

	/**
	 * 描述JSON对象的内容<br>
	 * 区别于toString()
	 * 
	 * @return
	 */
	String description();
}
