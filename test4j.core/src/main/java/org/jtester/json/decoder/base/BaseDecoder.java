package org.jtester.json.decoder.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.jtester.json.decoder.IDecoder;
import org.jtester.tools.commons.ClazzHelper;

/**
 * json串解码器基类<br>
 * 解码：从json字符串反序列为java对象<br>
 * 加码：将java对象序列化为json字符串<br>
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class BaseDecoder implements IDecoder {

	protected DecoderException getError(String value, String to) {
		String msg = String.format("couldn't convert \"%s\" to %s", value, to);
		return new DecoderException(msg);
	}

	protected DecoderException getError(String value, String to, Throwable e) {
		String msg = String.format("couldn't convert \"%s\" to %s", value, to);
		return new DecoderException(msg, e);
	}

	/**
	 * 获取第n个泛型变量，n从0开始
	 * 
	 * @param type
	 * @param index
	 * @return
	 */
	protected Type getArgType(ParameterizedType type, int index) {
		if (type == null || index < 0) {
			return null;
		}
		Type[] types = type.getActualTypeArguments();
		if (types == null || index >= types.length) {
			return null;
		}
		return types[index];
	}

	protected Class getRawType(Type type, Class _default) {
		if (type instanceof Class) {
			return (Class) type;
		} else if (type instanceof ParameterizedType) {
			Type raw = ((ParameterizedType) type).getRawType();
			return this.getRawType(raw, _default);
		} else {
			return _default == null ? Object.class : _default;
		}
	}

	protected boolean isInterfaceOrAbstract(Type type) {
		Class raw = this.getRawType(type, null);
		return ClazzHelper.isInterfaceOrAbstract(raw);
	}

	protected <T> T newInstance(Type type) {
		Class claz = this.getRawType(type, null);
		Object o = ClazzHelper.newInstance(claz);
		return (T) o;
	}
}
