package org.jtester.module.dbfit.fixture.dto;

import java.util.List;
import java.util.Map;

import org.jtester.module.dbfit.utility.ParseArg;
import org.jtester.tools.commons.MethodHelper;
import org.jtester.tools.commons.StringHelper;
import org.jtester.tools.exception.NoSuchMethodRuntimeException;
import org.jtester.tools.reflector.FieldAccessor;
import org.jtester.tools.reflector.MethodAccessor;

import fit.Parse;
import fit.TypeAdapter;
import fit.exception.FitFailureException;

@SuppressWarnings({ "rawtypes" })
public class DtoPropertyFixture extends SpringFixture {
	private Class dtoClazz;
	private MethodAccessor accessor;
	private PropertyBinding bindings[];
	private String parsePropertyMethodName;

	public void doRows(Parse rows) {
		Parse first = rows.parts;
		if (first == null) {
			throw new FitFailureException("You must specify a method.");
		}
		findMethod(first);
		Parse rest = rows.more;
		if (rest == null || rest.parts == null) {
			throw new RuntimeException("No test case is found!");
		}
		bindDtoProperties(rest.parts);
		super.doRows(rest.more);
	}

	/**
	 * 查找方法参数的class值<br>
	 * 如果在fit文件中指定了参数类型，那么返回指定了类型<br>
	 * 否则，查找名为methodname的唯一方法，并且返回参数的类型
	 * 
	 * @param methodname
	 * @param methodArg
	 * @return
	 */
	protected void findMethod(Parse cell) {
		String methodname = cell.text();
		methodname = StringHelper.camel(methodname);
		Parse argument = cell.more;
		if (StringHelper.isBlankOrNull(methodname)) {
			throw new FitFailureException("You must specify a method.");
		}
		try {
			if (argument == null) {
				this.accessor = MethodHelper.findMethodByArgCount(this, methodname, 1);
				this.dtoClazz = this.accessor.getMethod().getParameterTypes()[0];
			} else {
				String clazzName = argument.text();
				this.dtoClazz = Class.forName(clazzName);
				this.accessor = new MethodAccessor<Object>(this, methodname, dtoClazz);
			}
			this.right(cell);
		} catch (Throwable e) {
			this.wrong(cell);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 绑定参数类型
	 * 
	 * @param dtoProps
	 */
	private void bindDtoProperties(Parse dtoProps) {
		int size = dtoProps.size();
		try {
			String prop = dtoProps.text().trim();
			if (size == 1 && prop.startsWith(">>")) {
				this.parsePropertyMethodName = camel("parse " + prop.substring(2));
				this.bindings = null;
			} else {
				this.parsePropertyMethodName = null;
				this.bindings = new PropertyBinding[size];
				for (int i = 0; dtoProps != null; i++, dtoProps = dtoProps.more) {
					String property = camel(dtoProps.text());
					bindings[i] = new PropertyBinding(property);
				}
			}
		} catch (Throwable e) {
			this.wrong(dtoProps);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void doRow(Parse row) {
		Parse cells = row.parts;
		try {
			Object para = this.instancePara(cells);
			Object result = accessor.invoke(this, new Object[] { para });
			if (!(result instanceof Boolean)) {
				this.right(cells);
			} else if ((Boolean) result == true) {
				this.right(cells);
			} else {
				this.wrong(cells);
			}
		} catch (Throwable e) {
			this.exception(cells, e);
		}
	}

	/**
	 * 根据当前行的值，初始化参数值
	 * 
	 * @param cells
	 * @return
	 * @throws Exception
	 */
	private Object instancePara(Parse cells) throws Exception {
		if (this.parsePropertyMethodName != null) {
			String text = cells.text();
			try {
				MethodAccessor methodAccessor = new MethodAccessor<Object>(this, parsePropertyMethodName, String.class);
				return methodAccessor.invoke(this, new Object[] { text });
			} catch (NoSuchMethodRuntimeException e) {
				TypeAdapter typeAdapter = TypeAdapter.on(this, dtoClazz);
				return typeAdapter.parse(text);
			}
		}
		Object obj = dtoClazz.newInstance();

		for (int i = 0; cells != null && i < bindings.length; i++, cells = cells.more) {
			PropertyBinding binding = bindings[i];
			String text = ParseArg.parseCellValue(cells);
			binding.setValue(obj, text);
		}
		return obj;
	}

	protected class PropertyBinding {
		private FieldAccessor fieldAccessor;
		private String parseMethodName;
		private TypeAdapter typeAdapter;

		public PropertyBinding(String property) throws Exception {
			this.fieldAccessor = new FieldAccessor(dtoClazz, camel(property));
			this.parseMethodName = camel("parse " + property);
			this.typeAdapter = TypeAdapter.on(DtoPropertyFixture.this, fieldAccessor.getFieldType());
		}

		public void setValue(Object obj, String value) throws Exception {
			try {
				MethodAccessor methodAccessor = new MethodAccessor<Object>(obj, parseMethodName, String.class);
				methodAccessor.invoke(obj, new Object[] { value });
			} catch (NoSuchMethodRuntimeException e) {
				Object _value = typeAdapter.parse(value);
				fieldAccessor.set(obj, _value);
			}
		}
	}

	/**
	 * 解析以";"分割的字符串为list对象
	 * 
	 * @param value
	 * @return
	 */
	protected static List<String> parseList(String value) {
		return ParseArg.parseList(value);
	}

	/**
	 * 解析以split分割的字符串为list对象
	 * 
	 * @param value
	 * @param split
	 * @return
	 */
	protected static List<String> parseList(String value, String split) {
		return ParseArg.parseList(value, split);
	}

	/**
	 * 解析默认的方式组装map数据的字符串,map entry用";"分割，key-value以":"分割<br>
	 * 例子: "key1:value1;key2:value2"
	 * 
	 * @param value
	 * @return
	 */
	protected static Map<String, String> parseMap(String value) {
		return ParseArg.parseMap(value);
	}

	/**
	 * 将字符串解析为map，map entry用entrySplit分割，key-value以keySplit分割
	 * 
	 * @param value
	 * @param entrySplit
	 * @param keySplit
	 * @return
	 */
	protected static Map<String, String> parseMap(String value, String entrySplit, String keySplit) {
		return ParseArg.parseMap(value, entrySplit, keySplit);
	}

	/**
	 * 解析json对象
	 * 
	 * @param <T>
	 * @param clazz
	 * @param json
	 * @return
	 */
	protected static <T> T paserJson(Class<T> clazz, String json) {
		return ParseArg.paserJson(clazz, json);
	}
}
