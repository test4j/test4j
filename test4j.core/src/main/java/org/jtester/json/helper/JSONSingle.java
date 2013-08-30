package org.jtester.json.helper;

/**
 * 单值对象
 * 
 * @author darui.wudr
 * 
 */
public class JSONSingle implements JSONObject {
	private static final long serialVersionUID = -5097957793259430504L;

	/**
	 * String <br>
	 * primitive type<br>
	 * Referenct Object<br>
	 */
	private String value;

	private boolean quotationMark;

	private int beginIndex;

	private int endIndex;

	public JSONSingle() {
		this.quotationMark = false;
	}

	public JSONSingle(String value) {
		this.value = value;
		this.quotationMark = false;
	}

	public JSONSingle(boolean quote) {
		this.quotationMark = quote;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	@Override
	public String toString() {
		return this.value;
	}

	public String description() {
		StringBuilder builder = new StringBuilder();
		builder.append("JSONValue{value = [");
		builder.append(value);

		builder.append("], quotation mark = [");
		builder.append(this.quotationMark);
		builder.append("], position at [");
		builder.append(this.beginIndex);
		builder.append(",");
		builder.append(this.endIndex);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * 将JSONValue转换为字符串
	 * 
	 * @return
	 */
	public String toStringValue() {
		if (this.value == null) {
			return null;
		}
		if (this.quotationMark == false && "null".equalsIgnoreCase(this.value.trim())) {
			return null;
		} else {
			return this.value;
		}
	}

	/**
	 * 将value值转换为原始类型
	 * 
	 * @return
	 */
	public Object toPrimitiveValue() {
		if (this.value == null) {
			return null;
		}
		this.value = this.value.trim();
		if (this.quotationMark) {
			return this.value;
		}
		if ("null".equalsIgnoreCase(this.value.trim())) {
			return null;
		}

		if (this.value.endsWith("L") || this.value.endsWith("l")) {
			try {
				return Long.valueOf(this.value.substring(0, this.value.length() - 1));
			} catch (Exception e) {
			}
		}
		try {
			return Integer.valueOf(this.value);
		} catch (Exception e) {
		}

		if (this.value.endsWith("F") || this.value.endsWith("f")) {
			try {
				return Float.valueOf(this.value.substring(0, this.value.length() - 1));
			} catch (Exception e) {
			}
		}
		try {
			return Double.valueOf(this.value);
		} catch (Exception e) {
		}
		if (this.value.equalsIgnoreCase("true")) {
			return true;
		}
		if (this.value.equalsIgnoreCase("false")) {
			return false;
		}
		return this.value;
	}

	public String toClazzName() {
		int index = this.value.indexOf('@');
		if (index >= 0) {
			return this.value.substring(0, index);
		} else {
			return this.value;
		}
	}

	public String toReferenceID() {
		int index = this.value.indexOf('@');
		if (index >= 0) {
			return this.value.substring(index);
		} else {
			return null;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JSONSingle other = (JSONSingle) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	/**
	 * 构造单值json对象
	 * 
	 * @param value
	 * @return
	 */
	public static JSONSingle newInstance(Object value) {
		if (value instanceof JSONSingle) {
			return (JSONSingle) value;
		}
		JSONSingle json = new JSONSingle();
		if (value instanceof String) {
			json.quotationMark = true;
			json.value = (String) value;
		} else {
			json.quotationMark = false;
			json.value = String.valueOf(value);
		}
		return json;
	}

	/**
	 * 将对象转换为JSON对象
	 * 
	 * @param o
	 * @return
	 */
	static JSONObject convertJSON(Object o) {
		if (o == null) {
			return null;
		}
		Object newo = o;
		if (!(o instanceof JSONObject)) {
			newo = JSONSingle.newInstance(o);
		}
		return (JSONObject) newo;
	}
}
