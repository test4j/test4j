/*
 * Copyright 1999-2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ext.test4j.apache.commons.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A set of {@link Collection} related utility methods.
 * 
 * @since 1.0
 * @author Rodney Waldhoff
 * @author Paul Jack
 * @author Stephen Colebourne
 * @author Steve Downey
 * @version $Revision: 1.18.2.2 $ $Date: 2004/05/22 12:14:02 $
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CollectionUtils {

	/**
	 * Please don't ever instantiate a <code>CollectionUtils</code>.
	 */
	public CollectionUtils() {
	}

	/**
	 * Returns a {@link Collection} containing the union of the given
	 * {@link Collection}s.
	 * <p>
	 * The cardinality of each element in the returned {@link Collection} will
	 * be equal to the maximum of the cardinality of that element in the two
	 * given {@link Collection}s.
	 * 
	 * @see Collection#addAll
	 */
	public static Collection union(final Collection a, final Collection b) {
		ArrayList list = new ArrayList();
		Map mapa = getCardinalityMap(a);
		Map mapb = getCardinalityMap(b);
		Set elts = new HashSet(a);
		elts.addAll(b);
		Iterator it = elts.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			for (int i = 0, m = Math.max(getFreq(obj, mapa), getFreq(obj, mapb)); i < m; i++) {
				list.add(obj);
			}
		}
		return list;
	}

	/**
	 * Returns a {@link Collection} containing the intersection of the given
	 * {@link Collection}s.
	 * <p>
	 * The cardinality of each element in the returned {@link Collection} will
	 * be equal to the minimum of the cardinality of that element in the two
	 * given {@link Collection}s.
	 * 
	 * @see Collection#retainAll
	 * @see #containsAny
	 */
	public static Collection intersection(final Collection a, final Collection b) {
		ArrayList list = new ArrayList();
		Map mapa = getCardinalityMap(a);
		Map mapb = getCardinalityMap(b);
		Set elts = new HashSet(a);
		elts.addAll(b);
		Iterator it = elts.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			for (int i = 0, m = Math.min(getFreq(obj, mapa), getFreq(obj, mapb)); i < m; i++) {
				list.add(obj);
			}
		}
		return list;
	}

	/**
	 * Returns a {@link Collection} containing the exclusive disjunction
	 * (symmetric difference) of the given {@link Collection}s.
	 * <p>
	 * The cardinality of each element <i>e</i> in the returned
	 * {@link Collection} will be equal to
	 * <tt>max(cardinality(<i>e</i>,<i>a</i>),cardinality(<i>e</i>,<i>b</i>)) - min(cardinality(<i>e</i>,<i>a</i>),cardinality(<i>e</i>,<i>b</i>))</tt>.
	 * <p>
	 * This is equivalent to
	 * <tt>{@link #subtract subtract}({@link #union union(a,b)},{@link #intersection intersection(a,b)})</tt>
	 * or
	 * <tt>{@link #union union}({@link #subtract subtract(a,b)},{@link #subtract subtract(b,a)})</tt>.
	 */
	public static Collection disjunction(final Collection a, final Collection b) {
		ArrayList list = new ArrayList();
		Map mapa = getCardinalityMap(a);
		Map mapb = getCardinalityMap(b);
		Set elts = new HashSet(a);
		elts.addAll(b);
		Iterator it = elts.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			for (int i = 0, m = ((Math.max(getFreq(obj, mapa), getFreq(obj, mapb))) - (Math.min(getFreq(obj, mapa),
					getFreq(obj, mapb)))); i < m; i++) {
				list.add(obj);
			}
		}
		return list;
	}

	/**
	 * Returns a {@link Collection} containing <tt><i>a</i> - <i>b</i></tt>. The
	 * cardinality of each element <i>e</i> in the returned {@link Collection}
	 * will be the cardinality of <i>e</i> in <i>a</i> minus the cardinality of
	 * <i>e</i> in <i>b</i>, or zero, whichever is greater.
	 * 
	 * @see Collection#removeAll
	 */
	public static Collection subtract(final Collection a, final Collection b) {
		ArrayList list = new ArrayList(a);
		Iterator it = b.iterator();
		while (it.hasNext()) {
			list.remove(it.next());
		}
		return list;
	}

	/**
	 * Returns <code>true</code> iff some element of <i>a</i> is also an element
	 * of <i>b</i> (or, equivalently, if some element of <i>b</i> is also an
	 * element of <i>a</i>). In other words, this method returns
	 * <code>true</code> iff the {@link #intersection} of <i>a</i> and <i>b</i>
	 * is not empty.
	 * 
	 * @since 2.1
	 * @param a
	 *            a non-<code>null</code> Collection
	 * @param b
	 *            a non-<code>null</code> Collection
	 * @return <code>true</code> iff the intersection of <i>a</i> and <i>b</i>
	 *         is non-empty
	 * @see #intersection
	 */
	public static boolean containsAny(final Collection a, final Collection b) {
		// TO DO: we may be able to optimize this by ensuring either a or b
		// is the larger of the two Collections, but I'm not sure which.
		for (Iterator iter = a.iterator(); iter.hasNext();) {
			if (b.contains(iter.next())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a {@link Map} mapping each unique element in the given
	 * {@link Collection} to an {@link Integer} representing the number of
	 * occurances of that element in the {@link Collection}. An entry that maps
	 * to <tt>null</tt> indicates that the element does not appear in the given
	 * {@link Collection}.
	 */
	public static Map getCardinalityMap(final Collection col) {
		HashMap count = new HashMap();
		Iterator it = col.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			Integer c = (Integer) (count.get(obj));
			if (null == c) {
				count.put(obj, new Integer(1));
			} else {
				count.put(obj, new Integer(c.intValue() + 1));
			}
		}
		return count;
	}

	/**
	 * Returns <tt>true</tt> iff <i>a</i> is a sub-collection of <i>b</i>, that
	 * is, iff the cardinality of <i>e</i> in <i>a</i> is less than or equal to
	 * the cardinality of <i>e</i> in <i>b</i>, for each element <i>e</i> in
	 * <i>a</i>.
	 * 
	 * @see #isProperSubCollection
	 * @see Collection#containsAll
	 */
	public static boolean isSubCollection(final Collection a, final Collection b) {
		Map mapa = getCardinalityMap(a);
		Map mapb = getCardinalityMap(b);
		Iterator it = a.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			if (getFreq(obj, mapa) > getFreq(obj, mapb)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns <tt>true</tt> iff the given {@link Collection}s contain exactly
	 * the same elements with exactly the same cardinality.
	 * <p>
	 * That is, iff the cardinality of <i>e</i> in <i>a</i> is equal to the
	 * cardinality of <i>e</i> in <i>b</i>, for each element <i>e</i> in
	 * <i>a</i> or <i>b</i>.
	 */
	public static boolean isEqualCollection(final Collection a, final Collection b) {
		if (a.size() != b.size()) {
			return false;
		} else {
			Map mapa = getCardinalityMap(a);
			Map mapb = getCardinalityMap(b);
			if (mapa.size() != mapb.size()) {
				return false;
			} else {
				Iterator it = mapa.keySet().iterator();
				while (it.hasNext()) {
					Object obj = it.next();
					if (getFreq(obj, mapa) != getFreq(obj, mapb)) {
						return false;
					}
				}
				return true;
			}
		}
	}

	/**
	 * Returns the number of occurrences of <i>obj</i> in <i>col</i>.
	 */
	public static int cardinality(Object obj, final Collection col) {
		int count = 0;
		Iterator it = col.iterator();
		while (it.hasNext()) {
			Object elt = it.next();
			if ((null == obj && null == elt) || obj.equals(elt)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Transform the collection by applying a Transformer to each element.
	 * <p>
	 * If the input collection or transformer is null, there is no change made.
	 * <p>
	 * This routine is best for Lists and uses set(), however it adapts for all
	 * Collections that support clear() and addAll().
	 * <p>
	 * If the input collection controls its input, such as a Set, and the
	 * Transformer creates duplicates (or are otherwise invalid), the collection
	 * may reduce in size due to calling this method.
	 * 
	 * @param collection
	 *            the collection to get the input from, may be null
	 * @param transformer
	 *            the transformer to perform, may be null
	 */
	public static void transform(Collection collection, Transformer transformer) {
		if (collection != null && transformer != null) {
			if (collection instanceof List) {
				List list = (List) collection;
				for (ListIterator iter = list.listIterator(); iter.hasNext();) {
					Object element = iter.next();
					iter.set(transformer.transform(element));
				}
			} else {
				Collection resultCollection = collect(collection, transformer);
				collection.clear();
				collection.addAll(resultCollection);
			}
		}
	}

	/**
	 * Transforms all elements from inputCollection with the given transformer
	 * and adds them to the outputCollection.
	 * <p>
	 * If the input transfomer is null, the result is an empty list.
	 * 
	 * @param inputCollection
	 *            the collection to get the input from, may not be null
	 * @param transformer
	 *            the transformer to use, may be null
	 * @return the transformed result (new list)
	 * @throws NullPointerException
	 *             if the input collection is null
	 */
	public static Collection collect(Collection inputCollection, Transformer transformer) {
		ArrayList answer = new ArrayList(inputCollection.size());
		collect(inputCollection, transformer, answer);
		return answer;
	}

	/**
	 * Transforms all elements from the inputIterator with the given transformer
	 * and adds them to the outputCollection.
	 * <p>
	 * If the input iterator or transfomer is null, the result is an empty list.
	 * 
	 * @param inputIterator
	 *            the iterator to get the input from, may be null
	 * @param transformer
	 *            the transformer to use, may be null
	 * @return the transformed result (new list)
	 */
	public static Collection collect(Iterator inputIterator, Transformer transformer) {
		ArrayList answer = new ArrayList();
		collect(inputIterator, transformer, answer);
		return answer;
	}

	/**
	 * Transforms all elements from inputCollection with the given transformer
	 * and adds them to the outputCollection.
	 * <p>
	 * If the input collection or transfomer is null, there is no change to the
	 * output collection.
	 * 
	 * @param inputCollection
	 *            the collection to get the input from, may be null
	 * @param transformer
	 *            the transformer to use, may be null
	 * @param outputCollection
	 *            the collection to output into, may not be null
	 * @return the outputCollection with the transformed input added
	 * @throws NullPointerException
	 *             if the output collection is null
	 */
	public static Collection collect(Collection inputCollection, final Transformer transformer,
			final Collection outputCollection) {
		if (inputCollection != null) {
			return collect(inputCollection.iterator(), transformer, outputCollection);
		}
		return outputCollection;
	}

	/**
	 * Transforms all elements from the inputIterator with the given transformer
	 * and adds them to the outputCollection.
	 * <p>
	 * If the input iterator or transfomer is null, there is no change to the
	 * output collection.
	 * 
	 * @param inputIterator
	 *            the iterator to get the input from, may be null
	 * @param transformer
	 *            the transformer to use, may be null
	 * @param outputCollection
	 *            the collection to output into, may not be null
	 * @return the outputCollection with the transformed input added
	 * @throws NullPointerException
	 *             if the output collection is null
	 */
	public static Collection collect(Iterator inputIterator, final Transformer transformer,
			final Collection outputCollection) {
		if (inputIterator != null && transformer != null) {
			while (inputIterator.hasNext()) {
				Object item = inputIterator.next();
				Object value = transformer.transform(item);
				outputCollection.add(value);
			}
		}
		return outputCollection;
	}

	/**
	 * Adds all elements in the iteration to the given collection.
	 * 
	 * @param collection
	 *            the collection to add to
	 * @param iterator
	 *            the iterator of elements to add, may not be null
	 * @throws NullPointerException
	 *             if the collection or iterator is null
	 */
	public static void addAll(Collection collection, Iterator iterator) {
		while (iterator.hasNext()) {
			collection.add(iterator.next());
		}
	}

	/**
	 * Adds all elements in the enumeration to the given collection.
	 * 
	 * @param collection
	 *            the collection to add to
	 * @param enumeration
	 *            the enumeration of elements to add, may not be null
	 * @throws NullPointerException
	 *             if the collection or enumeration is null
	 */
	public static void addAll(Collection collection, Enumeration enumeration) {
		while (enumeration.hasMoreElements()) {
			collection.add(enumeration.nextElement());
		}
	}

	/**
	 * Adds all elements in the array to the given collection.
	 * 
	 * @param collection
	 *            the collection to add to
	 * @param elements
	 *            the array of elements to add, may be null
	 * @throws NullPointerException
	 *             if the collection or array is null
	 */
	public static void addAll(Collection collection, Object[] elements) {
		for (int i = 0, size = elements.length; i < size; i++) {
			collection.add(elements[i]);
		}
	}

	/**
	 * Given an Object, and an index, it will get the nth value in the object.
	 * <ul>
	 * <li>If obj is a Map, get the nth value from the <b>key</b> iterator.
	 * <li>If obj is a List or an array, get the nth value.
	 * <li>If obj is an iterator, enumeration or Collection, get the nth value
	 * from the iterator.
	 * <li>Return the original obj.
	 * </ul>
	 * 
	 * @param obj
	 *            the object to get an index of
	 * @param index
	 *            the index to get
	 * @throws IndexOutOfBoundsException
	 * @throws NoSuchElementException
	 */
	public static Object index(Object obj, int idx) {
		return index(obj, new Integer(idx));
	}

	/**
	 * Given an Object, and a key (index), it will get value associated with
	 * that key in the Object. The following checks are made:
	 * <ul>
	 * <li>If obj is a Map, use the index as a key to get a value. If no match
	 * continue.
	 * <li>Check key is an Integer. If not, return the object passed in.
	 * <li>If obj is a Map, get the nth value from the <b>key</b> iterator.
	 * <li>If obj is a List or an array, get the nth value.
	 * <li>If obj is an iterator, enumeration or Collection, get the nth value
	 * from the iterator.
	 * <li>Return the original obj.
	 * </ul>
	 * 
	 * @param obj
	 *            the object to get an index of
	 * @param index
	 *            the index to get
	 * @return the object at the specified index
	 * @throws IndexOutOfBoundsException
	 * @throws NoSuchElementException
	 */
	public static Object index(Object obj, Object index) {
		if (obj instanceof Map) {
			Map map = (Map) obj;
			if (map.containsKey(index)) {
				return map.get(index);
			}
		}
		int idx = -1;
		if (index instanceof Integer) {
			idx = ((Integer) index).intValue();
		}
		if (idx < 0) {
			return obj;
		} else if (obj instanceof Map) {
			Map map = (Map) obj;
			Iterator iterator = map.keySet().iterator();
			return index(iterator, idx);
		} else if (obj instanceof List) {
			return ((List) obj).get(idx);
		} else if (obj instanceof Object[]) {
			return ((Object[]) obj)[idx];
		} else if (obj instanceof Enumeration) {
			Enumeration enumeration = (Enumeration) obj;
			while (enumeration.hasMoreElements()) {
				idx--;
				if (idx == -1) {
					return enumeration.nextElement();
				} else {
					enumeration.nextElement();
				}
			}
		} else if (obj instanceof Iterator) {
			return index((Iterator) obj, idx);
		} else if (obj instanceof Collection) {
			Iterator iterator = ((Collection) obj).iterator();
			return index(iterator, idx);
		}
		return obj;
	}

	private static Object index(Iterator iterator, int idx) {
		while (iterator.hasNext()) {
			idx--;
			if (idx == -1) {
				return iterator.next();
			} else {
				iterator.next();
			}
		}
		return iterator;
	}

	/** Reverses the order of the given array */
	public static void reverseArray(Object[] array) {
		int i = 0;
		int j = array.length - 1;
		Object tmp;

		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	private static final int getFreq(final Object obj, final Map freqMap) {
		try {
			return ((Integer) (freqMap.get(obj))).intValue();
		} catch (NullPointerException e) {
			// ignored
		} catch (NoSuchElementException e) {
			// ignored
		}
		return 0;
	}

	/**
	 * Base class for collection decorators. I decided to do it this way because
	 * it seemed to result in the most reuse.
	 * 
	 * Inner class tree looks like:
	 * 
	 * <pre>
	 *       CollectionWrapper
	 *          PredicatedCollection
	 *             PredicatedSet
	 *             PredicatedList
	 *             PredicatedBag
	 *             PredicatedBuffer
	 *          UnmodifiableCollection
	 *             UnmodifiableBag
	 *             UnmodifiableBuffer
	 *          LazyCollection
	 *             LazyList
	 *             LazyBag
	 *       SynchronizedCollection
	 *          SynchronizedBuffer
	 *          SynchronizedBag
	 *          SynchronizedBuffer
	 * </pre>
	 */
	static class CollectionWrapper implements Collection {

		protected final Collection collection;

		public CollectionWrapper(Collection collection) {
			if (collection == null) {
				throw new IllegalArgumentException("Collection must not be null");
			}
			this.collection = collection;
		}

		public int size() {
			return collection.size();
		}

		public boolean isEmpty() {
			return collection.isEmpty();
		}

		public boolean contains(Object o) {
			return collection.contains(o);
		}

		public Iterator iterator() {
			return collection.iterator();
		}

		public Object[] toArray() {
			return collection.toArray();
		}

		public Object[] toArray(Object[] o) {
			return collection.toArray(o);
		}

		public boolean add(Object o) {
			return collection.add(o);
		}

		public boolean remove(Object o) {
			return collection.remove(o);
		}

		public boolean containsAll(Collection c2) {
			return collection.containsAll(c2);
		}

		public boolean addAll(Collection c2) {
			return collection.addAll(c2);
		}

		public boolean removeAll(Collection c2) {
			return collection.removeAll(c2);
		}

		public boolean retainAll(Collection c2) {
			return collection.retainAll(c2);
		}

		public void clear() {
			collection.clear();
		}

		public boolean equals(Object o) {
			if (o == this)
				return true;
			return collection.equals(o);
		}

		public int hashCode() {
			return collection.hashCode();
		}

		public String toString() {
			return collection.toString();
		}

	}

	static class UnmodifiableCollection extends CollectionWrapper {

		public UnmodifiableCollection(Collection c) {
			super(c);
		}

		public boolean add(Object o) {
			throw new UnsupportedOperationException();
		}

		public boolean addAll(Collection c) {
			throw new UnsupportedOperationException();
		}

		public boolean remove(Object o) {
			throw new UnsupportedOperationException();
		}

		public boolean removeAll(Collection c) {
			throw new UnsupportedOperationException();
		}

		public boolean retainAll(Collection c) {
			throw new UnsupportedOperationException();
		}

		public void clear() {
			throw new UnsupportedOperationException();
		}

		public Iterator iterator() {
			return new UnmodifiableIterator(collection.iterator());
		}

	}

	static class SynchronizedCollection {

		protected final Collection collection;

		public SynchronizedCollection(Collection collection) {
			if (collection == null) {
				throw new IllegalArgumentException("Collection must not be null");
			}
			this.collection = collection;
		}

		public synchronized int size() {
			return collection.size();
		}

		public synchronized boolean isEmpty() {
			return collection.isEmpty();
		}

		public synchronized boolean contains(Object o) {
			return collection.contains(o);
		}

		public Iterator iterator() {
			return collection.iterator();
		}

		public synchronized Object[] toArray() {
			return collection.toArray();
		}

		public synchronized Object[] toArray(Object[] o) {
			return collection.toArray(o);
		}

		public synchronized boolean add(Object o) {
			return collection.add(o);
		}

		public synchronized boolean remove(Object o) {
			return collection.remove(o);
		}

		public synchronized boolean containsAll(Collection c2) {
			return collection.containsAll(c2);
		}

		public synchronized boolean addAll(Collection c2) {
			return collection.addAll(c2);
		}

		public synchronized boolean removeAll(Collection c2) {
			return collection.removeAll(c2);
		}

		public synchronized boolean retainAll(Collection c2) {
			return collection.retainAll(c2);
		}

		public synchronized void clear() {
			collection.clear();
		}

		public synchronized boolean equals(Object o) {
			return collection.equals(o);
		}

		public synchronized int hashCode() {
			return collection.hashCode();
		}

		public synchronized String toString() {
			return collection.toString();
		}

	}

	static class UnmodifiableIterator implements Iterator {

		protected final Iterator iterator;

		public UnmodifiableIterator(Iterator iterator) {
			if (iterator == null) {
				throw new IllegalArgumentException("Iterator must not be null");
			}
			this.iterator = iterator;
		}

		public boolean hasNext() {
			return iterator.hasNext();
		}

		public Object next() {
			return iterator.next();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
