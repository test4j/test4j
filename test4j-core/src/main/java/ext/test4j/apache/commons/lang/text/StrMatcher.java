/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ext.test4j.apache.commons.lang.text;

import java.util.Arrays;

/**
 * A matcher class that can be queried to determine if a character array portion
 * matches.
 * <p>
 * This class comes complete with various factory methods. If these do not
 * suffice, you can subclass and implement your own matcher.
 * 
 * @author Apache Software Foundation
 * @since 2.2
 * @version $Id: StrMatcher.java 905636 2010-02-02 14:03:32Z niallp $
 */
public abstract class StrMatcher {
	/**
	 * Matches no characters.
	 */
	private static final StrMatcher NONE_MATCHER = new NoMatcher();

	/**
	 * Constructor that creates a matcher from a string.
	 * 
	 * @param str
	 *            the string to match, null or empty matches nothing
	 * @return a new Matcher for the given String
	 */
	public static StrMatcher stringMatcher(String str) {
		if (str == null || str.length() == 0) {
			return NONE_MATCHER;
		}
		return new StringMatcher(str);
	}

	/**
	 * Constructor.
	 */
	protected StrMatcher() {
		super();
	}

	/**
	 * Returns the number of matching characters, zero for no match.
	 * <p>
	 * This method is called to check for a match. The parameter
	 * <code>pos</code> represents the current position to be checked in the
	 * string <code>buffer</code> (a character array which must not be changed).
	 * The API guarantees that <code>pos</code> is a valid index for
	 * <code>buffer</code>.
	 * <p>
	 * The character array may be larger than the active area to be matched.
	 * Only values in the buffer between the specifed indices may be accessed.
	 * <p>
	 * The matching code may check one character or many. It may check
	 * characters preceeding <code>pos</code> as well as those after, so long as
	 * no checks exceed the bounds specified.
	 * <p>
	 * It must return zero for no match, or a positive number if a match was
	 * found. The number indicates the number of characters that matched.
	 * 
	 * @param buffer
	 *            the text content to match against, do not change
	 * @param pos
	 *            the starting position for the match, valid for buffer
	 * @param bufferEnd
	 *            the end index (exclusive) of the active buffer, valid for
	 *            buffer
	 * @return the number of matching characters, zero for no match
	 */
	public abstract int isMatch(char[] buffer, int pos, int bufferEnd);

	/**
	 * Class used to define a set of characters for matching purposes.
	 */
	static final class StringMatcher extends StrMatcher {
		/** The string to match, as a character array. */
		private final char[] chars;

		/**
		 * Constructor that creates a matcher from a String.
		 * 
		 * @param str
		 *            the string to match, must not be null
		 */
		StringMatcher(String str) {
			super();
			chars = str.toCharArray();
		}

		/**
		 * Returns whether or not the given text matches the stored string.
		 * 
		 * @param buffer
		 *            the text content to match against, do not change
		 * @param pos
		 *            the starting position for the match, valid for buffer
		 * @param bufferEnd
		 *            the end index of the active buffer, valid for buffer
		 * @return the number of matching characters, zero for no match
		 */
		public int isMatch(char[] buffer, int pos, int bufferEnd) {
			int len = chars.length;
			if (pos + len > bufferEnd) {
				return 0;
			}
			for (int i = 0; i < chars.length; i++, pos++) {
				if (chars[i] != buffer[pos]) {
					return 0;
				}
			}
			return len;
		}
	}

	/**
	 * Class used to match no characters.
	 */
	static final class NoMatcher extends StrMatcher {

		/**
		 * Constructs a new instance of <code>NoMatcher</code>.
		 */
		NoMatcher() {
			super();
		}

		/**
		 * Always returns <code>false</code>.
		 * 
		 * @param buffer
		 *            the text content to match against, do not change
		 * @param pos
		 *            the starting position for the match, valid for buffer
		 * @param bufferEnd
		 *            the end index of the active buffer, valid for buffer
		 * @return the number of matching characters, zero for no match
		 */
		public int isMatch(char[] buffer, int pos, int bufferEnd) {
			return 0;
		}
	}
}