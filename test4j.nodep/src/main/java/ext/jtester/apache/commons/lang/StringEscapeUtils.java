package ext.jtester.apache.commons.lang;

import ext.jtester.apache.commons.lang.Entities;

public class StringEscapeUtils {
	/**
	 * <p>
	 * Escapes the characters in a <code>String</code> using XML entities.
	 * </p>
	 * 
	 * <p>
	 * For example: <tt>"bread" & "butter"</tt> =>
	 * <tt>&amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;</tt>.
	 * </p>
	 * 
	 * <p>
	 * Supports only the five basic XML entities (gt, lt, quot, amp, apos). Does
	 * not support DTDs or external entities.
	 * </p>
	 * 
	 * <p>
	 * Note that unicode characters greater than 0x7f are currently escaped to
	 * their numerical \\u equivalent. This may change in future releases.
	 * </p>
	 * 
	 * @param str
	 *            the <code>String</code> to escape, may be null
	 * @return a new escaped <code>String</code>, <code>null</code> if null
	 *         string input
	 * @see #unescapeXml(java.lang.String)
	 */
	public static String escapeXml(String str) {
		if (str == null) {
			return null;
		}
		return Entities.XML.escape(str);
	}
}
