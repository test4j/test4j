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

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Substitutes variables within a string by values.
 * <p>
 * This class takes a piece of text and substitutes all the variables within it.
 * The default definition of a variable is <code>${variableName}</code>. The
 * prefix and suffix can be changed via constructors and set methods.
 * <p>
 * Variable values are typically resolved from a map, but could also be resolved
 * from system properties, or by supplying a custom variable resolver.
 * <p>
 * The simplest example is to use this class to replace Java System properties.
 * For example:
 *
 * <pre>
 * StrSubstitutor.replaceSystemProperties(&quot;You are running with java.version = ${java.version} and os.name = ${os.name}.&quot;);
 * </pre>
 * <p>
 * Typical usage of this class follows the following pattern: First an instance
 * is created and initialized with the map that contains the values for the
 * available variables. If a prefix and/or suffix for variables should be used
 * other than the default ones, the appropriate settings can be performed. After
 * that the <code>replace()</code> method can be called passing in the source
 * text for interpolation. In the returned text all variable references (as long
 * as their values are known) will be resolved. The following example
 * demonstrates this:
 *
 * <pre>
 * Map valuesMap = HashMap();
 * valuesMap.put(&quot;animal&quot;, &quot;quick brown fox&quot;);
 * valuesMap.put(&quot;target&quot;, &quot;lazy dog&quot;);
 * String templateString = &quot;The ${animal} jumped over the ${target}.&quot;;
 * StrSubstitutor sub = new StrSubstitutor(valuesMap);
 * String resolvedString = sub.replace(templateString);
 * </pre>
 * <p>
 * yielding:
 *
 * <pre>
 *      The quick brown fox jumped over the lazy dog.
 * </pre>
 * <p>
 * In addition to this usage pattern there are some static convenience methods
 * that cover the most common use cases. These methods can be used without the
 * need of manually creating an instance. However if multiple replace operations
 * are to be performed, creating and reusing an instance of this class will be
 * more efficient.
 * <p>
 * Variable replacement works in a recursive way. Thus, if a variable value
 * contains a variable then that variable will also be replaced. Cyclic
 * replacements are detected and will cause an exception to be thrown.
 * <p>
 * Sometimes the interpolation's result must contain a variable prefix. As an
 * example take the following source text:
 *
 * <pre>
 *   The variable ${${name}} must be used.
 * </pre>
 * <p>
 * Here only the variable's name refered to in the text should be replaced
 * resulting in the text (assuming that the value of the <code>name</code>
 * variable is <code>x</code>):
 *
 * <pre>
 *   The variable ${x} must be used.
 * </pre>
 * <p>
 * To achieve this effect there are two possibilities: Either set a different
 * prefix and suffix for variables which do not conflict with the result text
 * you want to produce. The other possibility is to use the escape character, by
 * default '$'. If this character is placed before a variable reference, this
 * reference is ignored and won't be replaced. For example:
 *
 * <pre>
 *   The variable $${${name}} must be used.
 * </pre>
 *
 * @author Apache Software Foundation
 * @author Oliver Heger
 * @version $Id: StrSubstitutor.java 905636 2010-02-02 14:03:32Z niallp $
 * @since 2.2
 */
public class StrSubstitutor {

    /**
     * Constant for the default escape character.
     */
    public static final char DEFAULT_ESCAPE = '$';
    /**
     * Constant for the default variable prefix.
     */
    public static final StrMatcher DEFAULT_PREFIX = StrMatcher.stringMatcher("${");
    /**
     * Constant for the default variable suffix.
     */
    public static final StrMatcher DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");

    /**
     * Stores the escape character.
     */
    private char escapeChar;
    /**
     * Stores the variable prefix.
     */
    private StrMatcher prefixMatcher;
    /**
     * Stores the variable suffix.
     */
    private StrMatcher suffixMatcher;
    /**
     * Variable resolution is delegated to an implementor of VariableResolver.
     */
    @Getter
    private Map<String, String> variableResolver;

    /**
     * Replaces all the occurrences of variables in the given source object with
     * their matching values from the map.
     *
     * @param source   the source text containing the variables to substitute, null
     *                 returns null
     * @param valueMap the map with the values, may be null
     * @return the result of the replace operation
     */
    public static String replace(Object source, Map valueMap) {
        return new StrSubstitutor(valueMap).replace(source);
    }

    /**
     * Creates a new instance and initializes it. Uses defaults for variable
     * prefix and suffix and the escaping character.
     *
     * @param valueMap the map with the variables' values, may be null
     */
    public StrSubstitutor(Map<String, String> valueMap) {
        this(valueMap, DEFAULT_PREFIX, DEFAULT_SUFFIX, DEFAULT_ESCAPE);
    }

    /**
     * Creates a new instance and initializes it.
     *
     * @param variableResolver the variable resolver, may be null
     * @param prefixMatcher    the prefix for variables, not null
     * @param suffixMatcher    the suffix for variables, not null
     * @param escape           the escape character
     * @throws IllegalArgumentException if the prefix or suffix is null
     */
    public StrSubstitutor(Map<String, String> variableResolver, StrMatcher prefixMatcher, StrMatcher suffixMatcher, char escape) {
        this.variableResolver = variableResolver;
        this.setVariablePrefixMatcher(prefixMatcher);
        this.setVariableSuffixMatcher(suffixMatcher);
        this.setEscapeChar(escape);
    }

    /**
     * Replaces all the occurrences of variables in the given source object with
     * their matching values from the resolver. The input source object is
     * converted to a string using <code>toString</code> and is not altered.
     *
     * @param source the source to replace in, null returns null
     * @return the result of the replace operation
     */
    public String replace(Object source) {
        if (source == null) {
            return null;
        }
        StrBuilder buf = new StrBuilder().append(source);
        substitute(buf, 0, buf.length());
        return buf.toString();
    }

    /**
     * Internal method that substitutes the variables.
     * <p>
     * Most users of this class do not need to call this method. This method
     * will be called automatically by another (public) method.
     * <p>
     * Writers of subclasses can override this method if they need access to the
     * substitution process at the start or end.
     *
     * @param buf    the string builder to substitute into, not null
     * @param offset the start offset within the builder, must be valid
     * @param length the length within the builder to be processed, must be valid
     * @return true if altered
     */
    protected boolean substitute(StrBuilder buf, int offset, int length) {
        return substitute(buf, offset, length, null) > 0;
    }

    /**
     * Recursive handler for multiple levels of interpolation. This is the main
     * interpolation method, which resolves the values of all variable
     * references contained in the passed in text.
     *
     * @param buf            the string builder to substitute into, not null
     * @param offset         the start offset within the builder, must be valid
     * @param length         the length within the builder to be processed, must be valid
     * @param priorVariables the stack keeping track of the replaced variables, may be null
     * @return the length change that occurs, unless priorVariables is null when
     * the int represents a boolean flag as to whether any change
     * occurred.
     */
    private int substitute(StrBuilder buf, int offset, int length, List priorVariables) {
        boolean top = (priorVariables == null);
        boolean altered = false;
        int lengthChange = 0;
        char[] chars = buf.buffer;
        int bufEnd = offset + length;
        int pos = offset;
        while (pos < bufEnd) {
            int startMatchLen = prefixMatcher.isMatch(chars, pos, bufEnd);
            if (startMatchLen == 0) {
                pos++;
            } else {
                // found variable start marker
                if (pos > offset && chars[pos - 1] == this.escapeChar) {
                    // escaped
                    buf.deleteCharAt(pos - 1);
                    chars = buf.buffer; // in case buffer was altered
                    lengthChange--;
                    altered = true;
                    bufEnd--;
                } else {
                    // find suffix
                    int startPos = pos;
                    pos += startMatchLen;
                    int endMatchLen = 0;
                    while (pos < bufEnd) {
                        endMatchLen = suffixMatcher.isMatch(chars, pos, bufEnd);
                        if (endMatchLen == 0) {
                            pos++;
                        } else {
                            // found variable end marker
                            String varName = new String(chars, startPos + startMatchLen, pos - startPos - startMatchLen);
                            pos += endMatchLen;
                            int endPos = pos;

                            // on the first call initialize priorVariables
                            if (priorVariables == null) {
                                priorVariables = new ArrayList();
                                priorVariables.add(new String(chars, offset, length));
                            }

                            // handle cyclic substitution
                            checkCyclicSubstitution(varName, priorVariables);
                            priorVariables.add(varName);

                            // resolve the variable
                            String varValue = resolveVariable(varName);
                            if (varValue != null) {
                                // recursive replace
                                int varLen = varValue.length();
                                buf.replace(startPos, endPos, varValue);
                                altered = true;
                                int change = substitute(buf, startPos, varLen, priorVariables);
                                change = change + (varLen - (endPos - startPos));
                                pos += change;
                                bufEnd += change;
                                lengthChange += change;
                                chars = buf.buffer; // in case buffer was
                                // altered
                            }

                            // remove variable from the cyclic stack
                            priorVariables.remove(priorVariables.size() - 1);
                            break;
                        }
                    }
                }
            }
        }
        if (top) {
            return (altered ? 1 : 0);
        }
        return lengthChange;
    }

    /**
     * Checks if the specified variable is already in the stack (list) of
     * variables.
     *
     * @param varName        the variable name to check
     * @param priorVariables the list of prior variables
     */
    private void checkCyclicSubstitution(String varName, List priorVariables) {
        if (priorVariables.contains(varName) == false) {
            return;
        }
        StrBuilder buf = new StrBuilder(256);
        buf.append("Infinite loop in property interpolation of ");
        buf.append(priorVariables.remove(0));
        buf.append(": ");
        buf.appendWithSeparators(priorVariables, "->");
        throw new IllegalStateException(buf.toString());
    }

    /**
     * Internal method that resolves the value of a variable.
     * <p>
     * Most users of this class do not need to call this method. This method is
     * called automatically by the substitution process.
     * <p>
     * Writers of subclasses can override this method if they need to alter how
     * each substitution occurs. The method is passed the variable's name and
     * must return the corresponding value. This implementation uses the
     * {@link #getVariableResolver()} with the variable's name as the key.
     *
     * @param variableName the name of the variable, not null
     * @return the variable's value or <b>null</b> if the variable is unknown
     */
    protected String resolveVariable(String variableName) {
        Map<String, String> resolver = getVariableResolver();
        if (resolver == null) {
            return null;
        }
        return resolver.get(variableName);
    }

    /**
     * Sets the escape character. If this character is placed before a variable
     * reference in the source text, this variable will be ignored.
     *
     * @param escapeCharacter the escape character (0 for disabling escaping)
     */
    public void setEscapeChar(char escapeCharacter) {
        this.escapeChar = escapeCharacter;
    }

    /**
     * Sets the variable prefix matcher currently in use.
     * <p>
     * The variable prefix is the characer or characters that identify the start
     * of a variable. This prefix is expressed in terms of a matcher allowing
     * advanced prefix matches.
     *
     * @param prefixMatcher the prefix matcher to use, null ignored
     * @return this, to enable chaining
     * @throws IllegalArgumentException if the prefix matcher is null
     */
    public StrSubstitutor setVariablePrefixMatcher(StrMatcher prefixMatcher) {
        if (prefixMatcher == null) {
            throw new IllegalArgumentException("Variable prefix matcher must not be null!");
        }
        this.prefixMatcher = prefixMatcher;
        return this;
    }

    /**
     * Sets the variable suffix matcher currently in use.
     * <p>
     * The variable suffix is the characer or characters that identify the end
     * of a variable. This suffix is expressed in terms of a matcher allowing
     * advanced suffix matches.
     *
     * @param suffixMatcher the suffix matcher to use, null ignored
     * @return this, to enable chaining
     * @throws IllegalArgumentException if the suffix matcher is null
     */
    public StrSubstitutor setVariableSuffixMatcher(StrMatcher suffixMatcher) {
        if (suffixMatcher == null) {
            throw new IllegalArgumentException("Variable suffix matcher must not be null!");
        }
        this.suffixMatcher = suffixMatcher;
        return this;
    }
}