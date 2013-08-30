package org.jtester.json.encoder.object;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.jtester.json.encoder.PropertyEncoder;
import org.jtester.json.encoder.ObjectEncoder;
import org.jtester.tools.commons.ClazzHelper;

/**
 * 按照pojo字段来输出json串
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class PoJoEncoder extends ObjectEncoder {

	public PoJoEncoder(Class clazz) {
		super(clazz == Object.class ? HashMap.class : clazz);
	}

	@Override
	protected Collection<PropertyEncoder> getPropertyEncoders(Object target) {
		List<PropertyEncoder> list = new ArrayList<PropertyEncoder>();
		if (target == null) {
			return list;
		}
		Class type = target.getClass();
		type = ClazzHelper.getUnProxyType(type);
		List<Field> fields = ClazzHelper.getAllFields(type, filterFields, false, true, false);
		for (Field field : fields) {
			if (skipFilterField(field.getName())) {
				continue;
			}
			PropertyEncoder encoder = PropertyEncoder.newInstance(field, target, features);
			encoder.setFeatures(features);
			list.add(encoder);
		}

		return list;
	}

	/**
	 * 是否是需要被过滤掉的字段
	 * 
	 * @param fieldname
	 * @return
	 */
	private boolean skipFilterField(String fieldname) {
		if (fieldname.startsWith("this$")) {
			return true;
		}
		if (filterFields.contains(fieldname)) {
			return true;
		}
		return false;
	}

	public static final List<String> filterFields = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			this.add("serialVersionUID");
			this.add("class");
		}
	};
}
