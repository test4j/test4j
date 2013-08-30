package org.jtester.json.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * json编码特性
 * 
 * @author darui.wudr
 * 
 */
public enum JSONFeature {
	/**
	 * 标记对应的class类型<br>
	 * eg: "#class:org.jtester.bean.User"
	 */
	UnMarkClassFlag,

	/**
	 * 给字段名称加引号<br>
	 * "fiendname":"fieldvalue"
	 */
	QuoteAllItems,

	/**
	 * 用单引号替代双引号<br>
	 * 'fieldname':'fieldvalue'
	 */
	UseSingleQuote,

	/**
	 * 忽略null字段和属性
	 */
	SkipNullValue,
	/**
	 * 是否忽略显式的字段类型输出<br>
	 * 当field字段类型等于实际对象的类型时
	 */
	IgnoreExplicitFieldType;

	private JSONFeature() {
		mask = (1 << ordinal());
	}

	private final int mask;

	public final int getMask() {
		return mask;
	}

	/**
	 * 根据掩码值判断某个特性是否有效
	 * 
	 * @param features
	 * @param feature
	 * @return
	 */
	public static boolean isEnabled(int features, JSONFeature feature) {
		return (features & feature.getMask()) != 0;
	}

	/**
	 * 返回特性值的掩码
	 * 
	 * @param features
	 * @return
	 */
	public static int getFeaturesMask(JSONFeature... features) {
		int marks = 0;
		for (JSONFeature feature : features) {
			int mark = feature.mask;
			marks = marks | mark;
		}
		return marks;
	}

	/**
	 * 根据掩码值返回特性列表
	 * 
	 * @param features
	 * @return
	 */
	public static List<JSONFeature> getSerializerFeatures(int features) {
		JSONFeature[] values = JSONFeature.values();
		List<JSONFeature> list = new ArrayList<JSONFeature>();
		for (JSONFeature value : values) {
			boolean isEnabled = isEnabled(features, value);
			if (isEnabled) {
				list.add(value);
			}
		}
		return list;
	}

	/**
	 * 类型标识
	 */
	public static final String ClazzFlag = "#class";

	/**
	 * 单值标识
	 */
	public static final String ValueFlag = "#value";

	/**
	 * 引用标识
	 */
	public static final String ReferFlag = "#refer";

	/**
	 * 异常标识
	 */
	public static final String ErrroFlag = "#error";
}
