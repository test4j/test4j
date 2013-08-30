package org.jtester.json.encoder;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jtester.json.JSONException;
import org.jtester.json.encoder.array.ArraysEncoder;
import org.jtester.json.encoder.array.CollectionEncoder;
import org.jtester.json.encoder.object.MapEncoder;
import org.jtester.json.encoder.object.PoJoEncoder;
import org.jtester.json.encoder.object.SpecEncoder;
import org.jtester.json.encoder.single.FixedTypeEncoder;
import org.jtester.json.encoder.single.SpecTypeEncoder;
import org.jtester.json.helper.ClazzMap;
import org.jtester.json.helper.JSONFeature;
import org.jtester.tools.commons.ClazzHelper;

/**
 * json串加码器基类<br>
 * 解码：从json字符串反序列为java对象<br>
 * 加码：将java对象序列化为json字符串<br>
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings("rawtypes")
public abstract class JSONEncoder<T> {

	protected int features;

	protected Class clazz;

	public JSONEncoder(Class clazz) {
		this.clazz = ClazzHelper.getUnProxyType(clazz);
	}

	/**
	 * json引号变量<br>
	 * 单引号或双引号
	 */
	protected char quote_Char = '"';

	/**
	 * 是否输出class类型
	 */
	protected boolean unMarkClassFlag = false;

	protected boolean ignoreExplicitFieldType = false;

	protected boolean skipNullField = false;

	public void setFeatures(int features) {
		this.features = features;

		boolean single = JSONFeature.isEnabled(features, JSONFeature.UseSingleQuote);
		quote_Char = single ? '\'' : '"';

		this.unMarkClassFlag = JSONFeature.isEnabled(features, JSONFeature.UnMarkClassFlag);
		this.ignoreExplicitFieldType = JSONFeature.isEnabled(features, JSONFeature.IgnoreExplicitFieldType);
		this.skipNullField = JSONFeature.isEnabled(features, JSONFeature.SkipNullValue);
	}

	/**
	 * 显示设置是否忽略输出class类型名称<br>
	 * 一般只有Field字段编码时会用到<br>
	 * 用于当Field.type = 实际对象类型时，可以选择忽略输出class类型名称
	 */
	public void setUnMarkClassFlag(boolean unMarkClassFlag) {
		this.unMarkClassFlag = unMarkClassFlag;
	}

	public void setFeatures(JSONFeature... jsonFeatures) {
		int features = JSONFeature.getFeaturesMask(jsonFeatures);
		this.setFeatures(features);
	}

	/**
	 * 将java对象序列化为json字符串
	 * 
	 * @param object
	 *            要序列化的对象
	 * @param writer
	 *            序列化字符串输出器
	 * @return TODO
	 */
	public abstract boolean encode(T target, Writer writer, List<String> references);

	public static JSONEncoder get(Class clazz) {
		FixedTypeEncoder finalTypeDecoder = FixedTypeEncoder.isFinalTypeEncoder(clazz);
		if (finalTypeDecoder != null) {
			return finalTypeDecoder;
		}
		SpecTypeEncoder specTypeEncoder = SpecTypeEncoder.isSpecTypeEncoder(clazz);
		if (specTypeEncoder != null) {
			return specTypeEncoder;
		}
		// 数组类型
		if (clazz.isArray()) {
			return ArraysEncoder.newInstance(clazz);
		}
		// 集合类型
		if (Collection.class.isAssignableFrom(clazz)) {
			return new CollectionEncoder(clazz);
		}
		// Map类型
		if (Map.class.isAssignableFrom(clazz)) {
			return new MapEncoder(clazz);
		}
		SpecEncoder specEncoder = SpecEncoder.isSpecPoJoEncoder(clazz);
		if (specEncoder != null) {
			return specEncoder;
		}
		// 普通对象
		return new PoJoEncoder(clazz);
	}

	/**
	 * 对象是null，或者引用了另外一个已经序列化的对象
	 * 
	 * @param value
	 *            要序列化的对象
	 * @param writer
	 * @param references
	 *            已序列化的对象地址标识列表
	 * @param isMarkRef
	 *            是否增加对象地址标识, 下列情形下不标识：<br>
	 *            o 简单对象<br>
	 *            o 有时不标识是为了防止多个地方调用结果自己引用了自己, 如果EntryEncoder中会判断一遍，再转到普通对象中编码<br>
	 * @return 是否已经输出了null值或者ref值
	 * @throws IOException
	 */
	protected boolean writerNullOrReference(Object value, Writer writer, List<String> references, boolean isMarkRef)
			throws IOException {
		if (value == null) {
			boolean skipNull = JSONFeature.isEnabled(features, JSONFeature.SkipNullValue);
			if (skipNull == false) {
				writer.append("null");
			}
			return true;
		}
		if (value instanceof Proxy) {
			writer.append("null");
			return true;
		}
		String address = ClazzMap.getReferenceAddress(value);
		if (references.contains(address)) {
			if (this.unMarkClassFlag) {
				writer.append("null");
			} else {
				writer.append('{');
				this.writerSpecProperty(JSONFeature.ReferFlag, writer);
				writer.append(':');
				writer.append(address);
				writer.append('}');
			}
			return true;
		}
		if (isMarkRef) {
			references.add(address);
		}
		return false;
	}

	/**
	 * 输出特定属性名称或值<br>
	 * 如果声明了QuoteFieldNames特性输出 "propertyname" 或 'propertyname' <br>
	 * 否则直接输出 propertyname
	 * 
	 * @param name
	 * @param writer
	 * @throws IOException
	 */
	protected void writerSpecProperty(String name, Writer writer) throws IOException {
		boolean quote = JSONFeature.isEnabled(features, JSONFeature.QuoteAllItems);
		if (quote == false) {
			writer.append(name);
		} else {
			writer.append(quote_Char).append(name).append(quote_Char);
		}
	}

	/**
	 * 标记class类型名称
	 * 
	 * @param writer
	 * @return
	 * @throws IOException
	 */
	protected boolean writeClassFlag(T target, Writer writer) throws IOException {
		if (target == null) {
			return false;
		}
		if (this.unMarkClassFlag) {
			return false;
		}

		String clazzname = ClazzMap.getClazzName(target);
		this.writerSpecProperty(JSONFeature.ClazzFlag, writer);
		writer.append(':');
		writer.append(quote_Char).append(clazzname).append(quote_Char);

		return true;
	}

	protected RuntimeException wrapException(Throwable e) {
		if (e instanceof Exception) {
			return (RuntimeException) e;
		} else {
			return new JSONException(e);
		}
	}
}
