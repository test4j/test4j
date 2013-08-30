package org.jtester.datafilling.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.jtester.datafilling.annotations.FillConstructor;
import org.jtester.datafilling.annotations.FillList;

@SuppressWarnings({ "rawtypes", "serial" })
public class ImmutableWithNonGenericCollectionsPojo implements Serializable {
	public static final int NBR_ELEMENTS = 5;

	private final Collection nonGenerifiedCollection;

	private final Map nonGenerifiedMap;

	private final Set nonGenerifiedSet;

	@FillConstructor
	public ImmutableWithNonGenericCollectionsPojo(@FillList(size = NBR_ELEMENTS) Collection nonGenerifiedCollection,
			@FillList(size = NBR_ELEMENTS) Map nonGenerifiedMap, @FillList(size = NBR_ELEMENTS) Set nonGenerifiedSet) {
		super();
		this.nonGenerifiedCollection = nonGenerifiedCollection;
		this.nonGenerifiedMap = nonGenerifiedMap;
		this.nonGenerifiedSet = nonGenerifiedSet;
	}

	public Collection getNonGenerifiedCollection() {
		return nonGenerifiedCollection;
	}

	public Map getNonGenerifiedMap() {
		return nonGenerifiedMap;
	}

	public Set getNonGenerifiedSet() {
		return nonGenerifiedSet;
	}
}
