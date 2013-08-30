package org.jtester.hamcrest.matcher.property.reflection;

import java.util.Map;

import org.jtester.hamcrest.matcher.property.difference.ClassDifference;
import org.jtester.hamcrest.matcher.property.difference.CollectionDifference;
import org.jtester.hamcrest.matcher.property.difference.Difference;
import org.jtester.hamcrest.matcher.property.difference.DifferenceVisitor;
import org.jtester.hamcrest.matcher.property.difference.MapDifference;
import org.jtester.hamcrest.matcher.property.difference.ObjectDifference;
import org.jtester.hamcrest.matcher.property.difference.UnorderedCollectionDifference;
import org.jtester.hamcrest.matcher.property.report.ObjectFormatter;

/**
 * A utility class to get the difference at the given element/field/key.
 */
public class InnerDifferenceFinder {

	/**
	 * Gets the difference at the given element/field/key (depending on the type
	 * of the given difference)
	 * 
	 * @param fieldName
	 *            The name of the element/field/key
	 * @param difference
	 *            The difference, not null
	 * @return The difference, null if there is no difference
	 */
	public static Difference getInnerDifference(String fieldName, Difference difference) {
		if (difference == null) {
			return null;
		}
		return difference.accept(new InnerDifferenceVisitor(), fieldName);
	}

	/**
	 * The visitor for visiting the difference tree.
	 */
	protected static class InnerDifferenceVisitor implements DifferenceVisitor<Difference, String> {

		/**
		 * Formatter for object values.
		 */
		protected ObjectFormatter objectFormatter = new ObjectFormatter();

		/**
		 * Returns null, there are no inner differences for a simple difference.
		 * 
		 * @param difference
		 *            The difference, not null
		 * @param key
		 *            The key
		 * @return null
		 */
		public Difference visit(Difference difference, String key) {
			return null;
		}

		/**
		 * Returns the difference at the field with the given name.
		 * 
		 * @param objectDifference
		 *            The difference, not null
		 * @param fieldName
		 *            The field name, not null
		 * @return The difference, null if there is no difference
		 */
		public Difference visit(ObjectDifference objectDifference, String fieldName) {
			return objectDifference.getFieldDifferences().get(fieldName);
		}

		public Difference visit(ClassDifference classDifference, String argument) {
			return null;
		}

		/**
		 * Returns the difference at the given key. The string represenation
		 * (using the object formatter) of the keys in the map are compared with
		 * the given key string.
		 * 
		 * @param mapDifference
		 *            The difference, not null
		 * @param keyString
		 *            The key as a string, not null
		 * @return The difference, null if there is no difference
		 */
		public Difference visit(MapDifference mapDifference, String keyString) {
			for (Map.Entry<Object, Difference> entry : mapDifference.getValueDifferences().entrySet()) {
				if (objectFormatter.format(entry.getKey()).equals(keyString)) {
					return entry.getValue();
				}
			}
			return null;
		}

		/**
		 * Returns the difference at the field with the given index.
		 * 
		 * @param collectionDifference
		 *            The difference, not null
		 * @param indexString
		 *            The index number as a string, not null
		 * @return The difference, null if there is no difference
		 */
		public Difference visit(CollectionDifference collectionDifference, String indexString) {
			return collectionDifference.getElementDifferences().get(new Integer(indexString));
		}

		/**
		 * Returns the best matching difference at the field with the given
		 * index.
		 * 
		 * @param unorderedCollectionDifference
		 *            The difference, not null
		 * @param indexString
		 *            The index number as a string, not null
		 * @return The difference, null if there is no difference
		 */
		public Difference visit(UnorderedCollectionDifference unorderedCollectionDifference, String indexString) {
			int leftIndex = new Integer(indexString);
			int rightIndex = unorderedCollectionDifference.getBestMatchingIndexes().get(leftIndex);
			return unorderedCollectionDifference.getElementDifference(leftIndex, rightIndex);
		}
	}

}