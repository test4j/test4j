package org.jtester.fortest.reflector;

@SuppressWarnings("unused")
public class SuperTestObject {

	private Boolean kind = Boolean.TRUE;

	private Boolean race = Boolean.FALSE;

	private int aPrivate = 26071973;

	private static int aSuperStaticPrivate = 27022008;

	private int getPrivate() {
		return aPrivate;
	}

	private int setPrivate(int newValue) {
		int oldValue = aPrivate;
		aPrivate = newValue;
		return oldValue;
	}
}
