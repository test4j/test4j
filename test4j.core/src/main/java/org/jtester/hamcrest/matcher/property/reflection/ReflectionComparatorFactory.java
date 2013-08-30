package org.jtester.hamcrest.matcher.property.reflection;

import static org.jtester.hamcrest.matcher.property.reflection.EqMode.IGNORE_DATES;
import static org.jtester.hamcrest.matcher.property.reflection.EqMode.EQ_STRING;
import static org.jtester.hamcrest.matcher.property.reflection.EqMode.IGNORE_DEFAULTS;
import static org.jtester.hamcrest.matcher.property.reflection.EqMode.IGNORE_ORDER;

import java.util.ArrayList;
import java.util.List;

import org.jtester.hamcrest.matcher.property.comparator.ListComparator;
import org.jtester.hamcrest.matcher.property.comparator.Comparator;
import org.jtester.hamcrest.matcher.property.comparator.EqStringComparator;
import org.jtester.hamcrest.matcher.property.comparator.HibernateProxyComparator;
import org.jtester.hamcrest.matcher.property.comparator.IgnoreDefaultsComparator;
import org.jtester.hamcrest.matcher.property.comparator.IgnoreDatesComparator;
import org.jtester.hamcrest.matcher.property.comparator.IgnoreNumberComparator;
import org.jtester.hamcrest.matcher.property.comparator.IgnoreOrderComparator;
import org.jtester.hamcrest.matcher.property.comparator.MapComparator;
import org.jtester.hamcrest.matcher.property.comparator.ObjectComparator;
import org.jtester.hamcrest.matcher.property.comparator.SimpleCasesComparator;
import org.jtester.tools.commons.ListHelper;

/**
 * A factory for creating a reflection comparator. This will assemble the
 * apropriate comparator chain and constructs a reflection comparator.
 * <p/>
 * By default, a strict comparison is performed, but if needed, some leniency
 * can be configured by setting one or more comparator modes:
 * <ul>
 * <li>ignore defaults: all fields that have a default java value for the left
 * object will be ignored. Eg if the left object contains an int field with
 * value 0 it will not be compared to the value of the right object.</li>
 * <li>lenient dates: only check whether both Date objects contain a value or
 * not, the value itself is not compared. Eg. if the left object contained a
 * date with value 1-1-2006 and the right object contained a date with value
 * 2-2-2006 they would still be considered equal.</li>
 * <li>lenient order: only check whether both collections or arrays contain the
 * same value, the actual order of the values is not compared. Eg. if the left
 * object is int[]{ 1, 2} and the right value is int[]{2, 1} they would still be
 * considered equal.
 */
public class ReflectionComparatorFactory {

	/**
	 * The LenientDatesComparator singleton insance
	 */
	protected static final Comparator LENIENT_DATES_COMPARATOR = new IgnoreDatesComparator();

	/**
	 * The object equals to String comparator
	 */
	protected static final Comparator EQ_STRING_COMPARATOR = new EqStringComparator();

	/**
	 * The IgnoreDefaultsComparator singleton insance
	 */
	protected static final Comparator IGNORE_DEFAULTS_COMPARATOR = new IgnoreDefaultsComparator();

	/**
	 * The LenientNumberComparator singleton insance
	 */
	protected static final Comparator LENIENT_NUMBER_COMPARATOR = new IgnoreNumberComparator();

	/**
	 * The SimpleCasesComparatorsingleton insance
	 */
	protected static final Comparator SIMPLE_CASES_COMPARATOR = new SimpleCasesComparator();

	/**
	 * The LenientOrderCollectionComparator singleton insance
	 */
	protected static final Comparator LENIENT_ORDER_COMPARATOR = new IgnoreOrderComparator();

	/**
	 * The CollectionComparator singleton insance
	 */
	protected static final Comparator COLLECTION_COMPARATOR = new ListComparator();

	/**
	 * The MapComparator singleton insance
	 */
	protected static final Comparator MAP_COMPARATOR = new MapComparator();

	/**
	 * The HibernateProxyComparator singleton insance
	 */
	protected static final Comparator HIBERNATE_PROXY_COMPARATOR = new HibernateProxyComparator();

	/**
	 * The ObjectComparator singleton insance
	 */
	protected static final Comparator OBJECT_COMPARATOR = new ObjectComparator();

	/**
	 * Creates a reflection comparator for the given modes. If no mode is given,
	 * a strict comparator will be created.
	 * 
	 * @param modes
	 *            The modes, null for strict comparison
	 * @return The reflection comparator, not null
	 */
	@SuppressWarnings("unchecked")
	public static ReflectionComparator createRefectionComparator(EqMode... modes) {
		List<EqMode> _modes = ListHelper.toList(modes);
		List<Comparator> comparators = getComparatorChain(_modes);
		return new ReflectionComparator(comparators);
	}

	/**
	 * Creates a comparator chain for the given modes. If no mode is given, a
	 * strict comparator will be created.
	 * 
	 * @param modes
	 *            The modes, null for strict comparison
	 * @return The comparator chain, not null
	 */
	protected static List<Comparator> getComparatorChain(List<EqMode> modes) {
		List<Comparator> comparatorChain = new ArrayList<Comparator>();
		if (modes.contains(IGNORE_DATES)) {
			comparatorChain.add(LENIENT_DATES_COMPARATOR);
		}
		if (modes.contains(EQ_STRING)) {
			comparatorChain.add(EQ_STRING_COMPARATOR);
		}
		if (modes.contains(IGNORE_DEFAULTS)) {
			comparatorChain.add(IGNORE_DEFAULTS_COMPARATOR);
		}
		comparatorChain.add(LENIENT_NUMBER_COMPARATOR);
		comparatorChain.add(SIMPLE_CASES_COMPARATOR);
		if (modes.contains(IGNORE_ORDER)) {
			comparatorChain.add(LENIENT_ORDER_COMPARATOR);
		} else {
			comparatorChain.add(COLLECTION_COMPARATOR);
		}
		comparatorChain.add(MAP_COMPARATOR);
		comparatorChain.add(HIBERNATE_PROXY_COMPARATOR);
		comparatorChain.add(OBJECT_COMPARATOR);
		return comparatorChain;
	}
}
