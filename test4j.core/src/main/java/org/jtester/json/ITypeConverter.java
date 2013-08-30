package org.jtester.json;

public interface ITypeConverter {
	/**
	 * 默认字符串到指定类型的转换器
	 */
	public static final ITypeConverter defaultConverter = new JSONConverter();

	<T> T convert(Object from);

	boolean accept(Object value);
}
