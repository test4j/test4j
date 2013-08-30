package org.jtester.datafilling.model.dto;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class MultiDimensionalConstructorPojo extends MultiDimensionalTestPojo {

	public MultiDimensionalConstructorPojo(List<List<List<String>>> threeDimensionalList,
			Set<Set<Set<Double>>> threeDimensionalSet,
			Collection<Collection<Collection<Long>>> threeDimensionalCollection,
			Map<Boolean, Map<Float, Map<Integer, Calendar>>> threeDimensionalMap,
			Queue<Queue<Queue<Date>>> threeDimensionalQueue, String[][][] threeDimensionalArray) {
		setThreeDimensionalArray(threeDimensionalArray);
		setThreeDimensionalCollection(threeDimensionalCollection);
		setThreeDimensionalList(threeDimensionalList);
		setThreeDimensionalMap(threeDimensionalMap);
		setThreeDimensionalQueue(threeDimensionalQueue);
		setThreeDimensionalSet(threeDimensionalSet);
	}
}
