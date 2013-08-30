package org.jtester.datafilling.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.jtester.datafilling.annotations.FillConstructor;
import org.jtester.datafilling.annotations.FillList;

public class ImmutableWithGenericCollectionsPojo implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int NBR_ELEMENTS = 5;

	private final Collection<OneDimensionalTestPojo> generifiedCollection;

	private final Map<String, Calendar> generifiedMap;

	private final Set<ImmutableWithNonGenericCollectionsPojo> generifiedSet;

	@FillConstructor
	public ImmutableWithGenericCollectionsPojo(
			@FillList(size = NBR_ELEMENTS) Collection<OneDimensionalTestPojo> generifiedCollection,
			@FillList(size = NBR_ELEMENTS) Map<String, Calendar> generifiedMap,
			@FillList(size = NBR_ELEMENTS) Set<ImmutableWithNonGenericCollectionsPojo> generifiedSet) {
		super();
		this.generifiedCollection = generifiedCollection;
		this.generifiedMap = generifiedMap;
		this.generifiedSet = generifiedSet;
	}

	public Collection<OneDimensionalTestPojo> getGenerifiedCollection() {
		return generifiedCollection;
	}

	public Map<String, Calendar> getGenerifiedMap() {
		return generifiedMap;
	}

	public Set<ImmutableWithNonGenericCollectionsPojo> getGenerifiedSet() {
		return generifiedSet;
	}

}
