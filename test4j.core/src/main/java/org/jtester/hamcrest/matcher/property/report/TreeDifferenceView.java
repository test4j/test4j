package org.jtester.hamcrest.matcher.property.report;

import static ext.jtester.apache.commons.lang.ClassUtils.getShortClassName;
import static org.jtester.hamcrest.matcher.property.report.DefaultDifferenceReport.MatchType.NO_MATCH;

import java.util.List;
import java.util.Map;

import org.jtester.hamcrest.matcher.property.difference.ClassDifference;
import org.jtester.hamcrest.matcher.property.difference.CollectionDifference;
import org.jtester.hamcrest.matcher.property.difference.Difference;
import org.jtester.hamcrest.matcher.property.difference.DifferenceVisitor;
import org.jtester.hamcrest.matcher.property.difference.MapDifference;
import org.jtester.hamcrest.matcher.property.difference.ObjectDifference;
import org.jtester.hamcrest.matcher.property.difference.UnorderedCollectionDifference;

/**
 * Formatter that will output all objects in the difference tree. For an
 * unordered collection difference, the best matching differences are taken.
 */
public class TreeDifferenceView implements DifferenceView {

	/**
	 * Formatter for object values.
	 */
	protected ObjectFormatter objectFormatter = new ObjectFormatter();

	/**
	 * The visitor for visiting the difference tree
	 */
	protected TreeDifferenceFormatterVisitor treeDifferenceFormatterVisitor = new TreeDifferenceFormatterVisitor();

	/**
	 * Creates a string representation of the given difference tree.
	 * 
	 * @param difference
	 *            The root difference, not null
	 * @return The string representation, not null
	 */
	public String createView(Difference difference) {
		return difference.accept(treeDifferenceFormatterVisitor, null);
	}

	/**
	 * Creates a string representation of a simple difference.
	 * 
	 * @param difference
	 *            The difference, not null
	 * @param fieldName
	 *            The current fieldName, null for root
	 * @return The string representation, not null
	 */
	protected String formatDifference(Difference difference, String fieldName) {
		return formatValues(fieldName, difference.getLeftValue(), difference.getRightValue());
	}

	/**
	 * Creates a string representation of an object difference.
	 * 
	 * @param objectDifference
	 *            The difference, not null
	 * @param fieldName
	 *            The current fieldName, null for root
	 * @return The string representation, not null
	 */
	protected String formatDifference(ObjectDifference objectDifference, String fieldName) {
		StringBuilder result = new StringBuilder();
		result.append(formatDifference((Difference) objectDifference, fieldName));

		for (Map.Entry<String, Difference> fieldDifference : objectDifference.getFieldDifferences().entrySet()) {
			String innerFieldName = createFieldName(fieldName, fieldDifference.getKey(), true);
			result.append(fieldDifference.getValue().accept(treeDifferenceFormatterVisitor, innerFieldName));
		}
		return result.toString();
	}

	protected String formatDifference(ClassDifference classDifference, String fieldName) {
		StringBuilder result = new StringBuilder();
		result.append((fieldName == null) ? "" : fieldName + ":");
		result.append("Expected: object of type ").append(getShortClassName(classDifference.getLeftClass()));
		result.append(", actual: object of type ").append(getShortClassName(classDifference.getRightClass()))
				.append("\n");
		return result.toString();
	}

	/**
	 * Creates a string representation of a collection difference.
	 * 
	 * @param collectionDifference
	 *            The difference, not null
	 * @param fieldName
	 *            The current fieldName, null for root
	 * @return The string representation, not null
	 */
	protected String formatDifference(CollectionDifference collectionDifference, String fieldName) {
		StringBuilder result = new StringBuilder();
		result.append(formatDifference((Difference) collectionDifference, fieldName));

		for (Map.Entry<Integer, Difference> elementDifferences : collectionDifference.getElementDifferences()
				.entrySet()) {
			String innerFieldName = createFieldName(fieldName, "[" + elementDifferences.getKey() + "]", false);
			result.append(elementDifferences.getValue().accept(treeDifferenceFormatterVisitor, innerFieldName));
		}

		List<?> leftList = collectionDifference.getLeftList();
		List<?> rightList = collectionDifference.getRightList();
		for (Integer leftIndex : collectionDifference.getLeftMissingIndexes()) {
			String innerFieldName = createFieldName(fieldName, "[" + leftIndex + "]", false);
			result.append(formatValues(innerFieldName, leftList.get(leftIndex), ""));
		}
		for (Integer rightIndex : collectionDifference.getRightMissingIndexes()) {
			String innerFieldName = createFieldName(fieldName, "[" + rightIndex + "]", false);
			result.append(formatValues(innerFieldName, "", rightList.get(rightIndex)));
		}
		return result.toString();
	}

	/**
	 * Creates a string representation of a map difference.
	 * 
	 * @param mapDifference
	 *            The difference, not null
	 * @param fieldName
	 *            The current fieldName, null for root
	 * @return The string representation, not null
	 */
	protected String formatDifference(MapDifference mapDifference, String fieldName) {
		StringBuilder result = new StringBuilder();
		result.append(formatDifference((Difference) mapDifference, fieldName));

		for (Map.Entry<Object, Difference> valueDifference : mapDifference.getValueDifferences().entrySet()) {
			String innerFieldName = createFieldName(fieldName, formatObject(valueDifference.getKey()), true);
			result.append(valueDifference.getValue().accept(treeDifferenceFormatterVisitor, innerFieldName));
		}

		Map<?, ?> leftMap = mapDifference.getLeftMap();
		Map<?, ?> rightMap = mapDifference.getRightMap();
		for (Object leftKey : mapDifference.getLeftMissingKeys()) {
			String innerFieldName = createFieldName(fieldName, formatObject(leftKey), true);
			result.append(formatValues(innerFieldName, leftMap.get(leftKey), ""));
		}
		for (Object rightKey : mapDifference.getRightMissingKeys()) {
			String innerFieldName = createFieldName(fieldName, formatObject(rightKey), true);
			result.append(formatValues(innerFieldName, rightMap.get(rightKey), ""));
		}
		return result.toString();
	}

	protected String formatObject(Object object) {
		if (object == NO_MATCH) {
			return "--no match--";
		}
		return objectFormatter.format(object);
	}

	/**
	 * Creates a string representation of an unorder collection difference.
	 * 
	 * @param unorderedCollectionDifference
	 *            The difference, not null
	 * @param fieldName
	 *            The current fieldName, null for root
	 * @return The string representation, not null
	 */
	protected String formatDifference(UnorderedCollectionDifference unorderedCollectionDifference, String fieldName) {
		StringBuilder result = new StringBuilder();
		result.append(formatDifference((Difference) unorderedCollectionDifference, fieldName));

		Map<Integer, Integer> bestMatchingIndexes = unorderedCollectionDifference.getBestMatchingIndexes();
		for (Map.Entry<Integer, Integer> bestMatchingIndex : bestMatchingIndexes.entrySet()) {
			int leftIndex = bestMatchingIndex.getKey();
			int rightIndex = bestMatchingIndex.getValue();

			if (leftIndex == -1) {
				String innerFieldName = createFieldName(fieldName, "[x," + rightIndex + "]", false);
				result.append(formatValues(innerFieldName, NO_MATCH,
						unorderedCollectionDifference.getRightList().get(rightIndex)));
				continue;
			}
			if (rightIndex == -1) {
				String innerFieldName = createFieldName(fieldName, "[" + leftIndex + ",x]", false);
				result.append(formatValues(innerFieldName, unorderedCollectionDifference.getLeftList().get(leftIndex),
						NO_MATCH));
				continue;
			}

			Difference difference = unorderedCollectionDifference.getElementDifference(leftIndex, rightIndex);
			if (difference == null) {
				continue;
			}

			String innerFieldName = createFieldName(fieldName, "[" + leftIndex + "," + rightIndex + "]", false);
			result.append(difference.accept(treeDifferenceFormatterVisitor, innerFieldName));
		}
		return result.toString();
	}

	/**
	 * Formats and appends the given fieldname and object values.
	 * 
	 * @param fieldName
	 *            The field name, null if there is no field name
	 * @param expectedValue
	 *            The left value
	 * @param actualValue
	 *            The right value
	 * @return The string representation, not null
	 */
	protected String formatValues(String fieldName, Object expectedValue, Object actualValue) {
		StringBuilder result = new StringBuilder();

		String prefix = (fieldName == null) ? "" : fieldName;
		result.append(prefix);
		result.append(" expected: ");
		result.append(formatObject(expectedValue));
		result.append("\n");
		result.append(prefix);
		result.append("   actual: ");
		result.append(formatObject(actualValue));
		result.append("\n\n");
		return result.toString();
	}

	/**
	 * Adds the inner field name to the given field name.
	 * 
	 * @param fieldName
	 *            The field
	 * @param innerFieldName
	 *            The field to append, not null
	 * @param includePoint
	 *            True if a point should be added
	 * @return The field name
	 */
	protected String createFieldName(String fieldName, String innerFieldName, boolean includePoint) {
		if (fieldName == null) {
			return innerFieldName;
		}
		StringBuilder result = new StringBuilder();
		result.append(fieldName);
		if (includePoint) {
			result.append(".");
		}
		result.append(innerFieldName);
		return result.toString();
	}

	/**
	 * The visitor for visiting the difference tree.
	 */
	protected class TreeDifferenceFormatterVisitor implements DifferenceVisitor<String, String> {

		public String visit(Difference difference, String fieldName) {
			return formatDifference(difference, fieldName);
		}

		public String visit(ObjectDifference objectDifference, String fieldName) {
			return formatDifference(objectDifference, fieldName);
		}

		public String visit(ClassDifference classDifference, String fieldName) {
			return formatDifference(classDifference, fieldName);
		}

		public String visit(MapDifference mapDifference, String fieldName) {
			return formatDifference(mapDifference, fieldName);
		}

		public String visit(CollectionDifference collectionDifference, String fieldName) {
			return formatDifference(collectionDifference, fieldName);
		}

		public String visit(UnorderedCollectionDifference unorderedCollectionDifference, String fieldName) {
			return formatDifference(unorderedCollectionDifference, fieldName);
		}
	}
}