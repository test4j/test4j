package org.test4j.json;


public class JSONConverter implements ITypeConverter {

	@SuppressWarnings("unchecked")
	public <T> T convert(Object from) {
		Object o = JSON.toObject(String.valueOf(from));
		return (T) o;
	}

	public boolean accept(Object value) {
		return true;
	}
}
