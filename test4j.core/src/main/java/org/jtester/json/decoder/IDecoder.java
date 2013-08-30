package org.jtester.json.decoder;

import java.lang.reflect.Type;
import java.util.Map;

import org.jtester.json.helper.JSONObject;

/**
 * json串解码器基类<br>
 * 解码：从json字符串反序列为java对象<br>
 * 加码：将java对象序列化为json字符串<br>
 * 
 * @author darui.wudr
 * 
 */
public interface IDecoder {
	/**
	 * 将json串反序列为对象
	 * 
	 * @param from
	 * @param toType
	 * @param references
	 * @return
	 */
	<T> T decode(JSONObject from, Type toType, Map<String, Object> references);

	/**
	 * 是否接受是该类型json解码器
	 * 
	 * @param type
	 * @return
	 */
	boolean accept(Type type);
}
