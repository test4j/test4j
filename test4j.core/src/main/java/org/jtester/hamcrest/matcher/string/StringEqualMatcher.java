package org.jtester.hamcrest.matcher.string;

/**
 * 按照模式来判断2个string是否相同
 * 
 * @author darui.wudr
 * 
 */
public class StringEqualMatcher extends StringMatcher {

	public StringEqualMatcher(String expected) {
		super(expected);
	}

	@Override
	protected boolean match(String expected, String actual) {
		return actual.equals(expected);
	}

	@Override
	protected String relationship() {
		return "is equals to by modes" + StringMode.toString(modes);
	}

}
