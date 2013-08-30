package org.jtester.json.encoder.beans.test;

public class GenicBean<T> {
	String name;

	T refObject;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public T getRefObject() {
		return refObject;
	}

	public void setRefObject(T refObject) {
		this.refObject = refObject;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static GenicBean newInstance(String name, Object ref) {
		GenicBean bean = new GenicBean();
		bean.name = name;
		bean.refObject = ref;
		return bean;
	}
}
