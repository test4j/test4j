package org.jtester.fortest.reflector;

@SuppressWarnings("unused")
public class ForReflectUtil {
	private boolean man = true;
	private String first;
	private String last;
	private String noGetMethod = "no get method field";

	private String myName = "my name";

	public ForReflectUtil(String first, String last) {
		this.first = first;
		this.last = last;
	}

	public boolean isMan() {
		return man;
	}

	public void setMan(boolean man) {
		this.man = man;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public String getMyName() {
		return first + "," + last;
	}

}
