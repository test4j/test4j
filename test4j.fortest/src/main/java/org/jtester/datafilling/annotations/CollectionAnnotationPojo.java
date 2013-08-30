package org.jtester.datafilling.annotations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jtester.datafilling.annotations.FillList;
import org.jtester.datafilling.utils.FillDataTestConstants;

public class CollectionAnnotationPojo implements Serializable {
	private static final long serialVersionUID = 1L;

	@FillList(size = FillDataTestConstants.ANNOTATION_COLLECTION_NBR_ELEMENTS)
	/** A collection with a specified number of elements */
	private List<String> strList = new ArrayList<String>();

	@FillList(size = FillDataTestConstants.ANNOTATION_COLLECTION_NBR_ELEMENTS)
	/** An array with a specified number of elements */
	private String[] strArray = new String[FillDataTestConstants.ANNOTATION_COLLECTION_NBR_ELEMENTS];

	@FillList(size = FillDataTestConstants.ANNOTATION_COLLECTION_NBR_ELEMENTS)
	/** A Map with a specified number of elements */
	private Map<String, String> stringMap = new HashMap<String, String>();

	/**
	 * @return the strList
	 */
	public List<String> getStrList() {
		return strList;
	}

	/**
	 * @param strList
	 *            the strList to set
	 */
	public void setStrList(List<String> strList) {
		this.strList = strList;
	}

	/**
	 * @return the strArray
	 */
	public String[] getStrArray() {
		return strArray;
	}

	/**
	 * @param strArray
	 *            the strArray to set
	 */
	public void setStrArray(String[] strArray) {
		this.strArray = strArray;
	}

	/**
	 * @return the stringMap
	 */
	public Map<String, String> getStringMap() {
		return stringMap;
	}

	/**
	 * @param stringMap
	 *            the stringMap to set
	 */
	public void setStringMap(Map<String, String> stringMap) {
		this.stringMap = stringMap;
	}
}
