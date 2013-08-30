package org.jtester.json.helper;

public final class SpecicalChar {

	public final static boolean[] specicalFlags = new boolean[256];

	public final static char[] replaceChars = new char[256];
	static {
		specicalFlags['\b'] = true;
		specicalFlags['\n'] = true;
		specicalFlags['\f'] = true;
		specicalFlags['\r'] = true;
		specicalFlags['\''] = true;
		specicalFlags['\\'] = true;

		replaceChars['\b'] = 'b';
		replaceChars['\n'] = 'n';
		replaceChars['\f'] = 'f';
		replaceChars['\r'] = 'r';
		replaceChars['\"'] = '"';
		replaceChars['\''] = '\'';
		replaceChars['\\'] = '\\';
	}
}
