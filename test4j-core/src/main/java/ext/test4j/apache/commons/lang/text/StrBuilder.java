package ext.test4j.apache.commons.lang.text;

import java.util.Collection;
import java.util.Iterator;

public class StrBuilder implements Cloneable {
    /**
     * The extra capacity for new builders.
     */
    static final int CAPACITY = 32;

    /**
     * Internal data storage.
     */
    protected char[] buffer;
    /**
     * Current size of the buffer.
     */
    private int size;

    /**
     * Constructor that creates an empty builder initial capacity 32 characters.
     */
    public StrBuilder() {
        this(CAPACITY);
    }

    /**
     * Constructor that creates an empty builder the specified initial capacity.
     *
     * @param initialCapacity the initial capacity, zero or less will be converted to 32
     */
    public StrBuilder(int initialCapacity) {
        super();
        if (initialCapacity <= 0) {
            initialCapacity = CAPACITY;
        }
        buffer = new char[initialCapacity];
    }

    /**
     * Gets the length of the string builder.
     *
     * @return the length
     */
    public int length() {
        return size;
    }

    /**
     * Checks the capacity and ensures that it is at least the size specified.
     *
     * @param capacity the capacity to ensure
     * @return this, to enable chaining
     */
    public StrBuilder ensureCapacity(int capacity) {
        if (capacity > buffer.length) {
            char[] old = buffer;
            buffer = new char[capacity * 2];
            System.arraycopy(old, 0, buffer, 0, size);
        }
        return this;
    }

    public StrBuilder deleteCharAt(int index) {
        if (index < 0 || index >= size) {
            throw new StringIndexOutOfBoundsException(index);
        }
        deleteImpl(index, index + 1, 1);
        return this;
    }

    /**
     * Appends the text representing <code>null</code> to this string builder.
     *
     * @return this, to enable chaining
     */
    public StrBuilder appendNull() {
        return append((String) null);
    }

    /**
     * Appends an object to this string builder. Appending null will call
     * {@link #appendNull()}.
     *
     * @param obj the object to append
     * @return this, to enable chaining
     */
    public StrBuilder append(Object obj) {
        if (obj == null) {
            return appendNull();
        }
        return append(obj.toString());
    }

    /**
     * Appends a string to this string builder. Appending null will call
     * {@link #appendNull()}.
     *
     * @param str the string to append
     * @return this, to enable chaining
     */
    public StrBuilder append(String str) {
        if (str == null) {
            return appendNull();
        }
        int strLen = str.length();
        if (strLen > 0) {
            int len = length();
            ensureCapacity(len + strLen);
            str.getChars(0, strLen, buffer, len);
            size += strLen;
        }
        return this;
    }

    /**
     * Appends a collection placing separators between each value, but not
     * before the first or after the last. Appending a null collection will have
     * no effect. Each object is appended using {@link #append(Object)}.
     *
     * @param coll      the collection to append
     * @param separator the separator to use, null means no separator
     * @return this, to enable chaining
     */
    public StrBuilder appendWithSeparators(Collection coll, String separator) {
        if (coll != null && coll.size() > 0) {
            separator = (separator == null ? "" : separator);
            Iterator it = coll.iterator();
            while (it.hasNext()) {
                append(it.next());
                if (it.hasNext()) {
                    append(separator);
                }
            }
        }
        return this;
    }

    /**
     * Internal method to delete a range without validation.
     *
     * @param startIndex the start index, must be valid
     * @param endIndex   the end index (exclusive), must be valid
     * @param len        the length, must be valid
     * @throws IndexOutOfBoundsException if any index is invalid
     */
    private void deleteImpl(int startIndex, int endIndex, int len) {
        System.arraycopy(buffer, endIndex, buffer, startIndex, size - endIndex);
        size -= len;
    }

    /**
     * Internal method to delete a range without validation.
     *
     * @param startIndex the start index, must be valid
     * @param endIndex   the end index (exclusive), must be valid
     * @param removeLen  the length to remove (endIndex - startIndex), must be valid
     * @param insertStr  the string to replace with, null means delete range
     * @param insertLen  the length of the insert string, must be valid
     * @throws IndexOutOfBoundsException if any index is invalid
     */
    private void replaceImpl(int startIndex, int endIndex, int removeLen, String insertStr, int insertLen) {
        int newSize = size - removeLen + insertLen;
        if (insertLen != removeLen) {
            ensureCapacity(newSize);
            System.arraycopy(buffer, endIndex, buffer, startIndex + insertLen, size - endIndex);
            size = newSize;
        }
        if (insertLen > 0) {
            insertStr.getChars(0, insertLen, buffer, startIndex);
        }
    }

    /**
     * Replaces a portion of the string builder with another string. The length
     * of the inserted string does not have to match the removed length.
     *
     * @param startIndex the start index, inclusive, must be valid
     * @param endIndex   the end index, exclusive, must be valid except that if too
     *                   large it is treated as end of string
     * @param replaceStr the string to replace with, null means delete range
     * @return this, to enable chaining
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public StrBuilder replace(int startIndex, int endIndex, String replaceStr) {
        endIndex = validateRange(startIndex, endIndex);
        int insertLen = (replaceStr == null ? 0 : replaceStr.length());
        replaceImpl(startIndex, endIndex, endIndex - startIndex, replaceStr, insertLen);
        return this;
    }

    /**
     * Checks the contents of this builder against another to see if they
     * contain the same character content.
     *
     * @param other the object to check, null returns false
     * @return true if the builders contain the same characters in the same
     * order
     */
    public boolean equals(StrBuilder other) {
        if (this == other) {
            return true;
        }
        if (this.size != other.size) {
            return false;
        }
        char thisBuf[] = this.buffer;
        char otherBuf[] = other.buffer;
        for (int i = size - 1; i >= 0; i--) {
            if (thisBuf[i] != otherBuf[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks the contents of this builder against another to see if they
     * contain the same character content.
     *
     * @param obj the object to check, null returns false
     * @return true if the builders contain the same characters in the same
     * order
     */
    public boolean equals(Object obj) {
        if (obj instanceof StrBuilder) {
            return equals((StrBuilder) obj);
        }
        return false;
    }

    /**
     * Gets a suitable hash code for this builder.
     *
     * @return a hash code
     */
    public int hashCode() {
        char buf[] = buffer;
        int hash = 0;
        for (int i = size - 1; i >= 0; i--) {
            hash = 31 * hash + buf[i];
        }
        return hash;
    }

    /**
     * Gets a String version of the string builder, creating a new instance each
     * time the method is called.
     * <p>
     * Note that unlike StringBuffer, the string version returned is independent
     * of the string builder.
     *
     * @return the builder as a String
     */
    public String toString() {
        return new String(buffer, 0, size);
    }

    /**
     * Validates parameters defining a range of the builder.
     *
     * @param startIndex the start index, inclusive, must be valid
     * @param endIndex   the end index, exclusive, must be valid except that if too
     *                   large it is treated as end of string
     * @return the new string
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    protected int validateRange(int startIndex, int endIndex) {
        if (startIndex < 0) {
            throw new StringIndexOutOfBoundsException(startIndex);
        }
        if (endIndex > size) {
            endIndex = size;
        }
        if (startIndex > endIndex) {
            throw new StringIndexOutOfBoundsException("end < start");
        }
        return endIndex;
    }
}