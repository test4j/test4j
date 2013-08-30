package org.jtester.datafilling.annotations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jtester.datafilling.annotations.FillList;
import org.jtester.datafilling.annotations.FillWith;
import org.jtester.datafilling.strategies.MyBirthdayStrategy;
import org.jtester.datafilling.strategies.PostCodeStrategy;

@SuppressWarnings("serial")
public class StrategyPojo implements Serializable {

	@FillWith(PostCodeStrategy.class)
	private String postCode;

	@FillWith(MyBirthdayStrategy.class)
	private Calendar myBirthday;

	@FillList(size = 2, collectionElementStrategy = MyBirthdayStrategy.class)
	private List<Calendar> myBirthdays = new ArrayList<Calendar>();

	@FillList(size = 2)
	private List<Object> objectList = new ArrayList<Object>();

	@FillList(size = 2, mapElementStrategy = MyBirthdayStrategy.class)
	private Map<String, Calendar> myBirthdaysMap = new HashMap<String, Calendar>();

	@SuppressWarnings("rawtypes")
	// This is intentional
	private List nonGenericObjectList = new ArrayList();

	@FillList(size = 2, collectionElementStrategy = MyBirthdayStrategy.class)
	private Calendar[] myBirthdaysArray;

	@FillList(size = 2)
	private Object[] myObjectArray;


	/**
	 * @return the postCode
	 */
	public String getPostCode() {
		return postCode;
	}

	/**
	 * @param postCode
	 *            the postCode to set
	 */
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	/**
	 * @return the myBirthday
	 */
	public Calendar getMyBirthday() {
		return myBirthday;
	}

	/**
	 * @param myBirthday
	 *            the myBirthday to set
	 */
	public void setMyBirthday(Calendar myBirthday) {
		this.myBirthday = myBirthday;
	}

	/**
	 * @return the myBirthdays
	 */
	public List<Calendar> getMyBirthdays() {
		return myBirthdays;
	}

	/**
	 * @param myBirthdays
	 *            the myBirthdays to set
	 */
	public void setMyBirthdays(List<Calendar> myBirthdays) {
		this.myBirthdays = myBirthdays;
	}

	/**
	 * @return the myBirthdaysArray
	 */
	public Calendar[] getMyBirthdaysArray() {
		return myBirthdaysArray;
	}

	/**
	 * @param myBirthdaysArray
	 *            the myBirthdaysArray to set
	 */
	public void setMyBirthdaysArray(Calendar[] myBirthdaysArray) {
		this.myBirthdaysArray = myBirthdaysArray;
	}

	/**
	 * @return the objectList
	 */
	public List<Object> getObjectList() {
		return objectList;
	}

	/**
	 * @param objectList
	 *            the objectList to set
	 */
	public void setObjectList(List<Object> objectList) {
		this.objectList = objectList;
	}

	/**
	 * @return the myObjectArray
	 */
	public Object[] getMyObjectArray() {
		return myObjectArray;
	}

	/**
	 * @param myObjectArray
	 *            the myObjectArray to set
	 */
	public void setMyObjectArray(Object[] myObjectArray) {
		this.myObjectArray = myObjectArray;
	}

	/**
	 * @return the nonGenericObjectList
	 */
	@SuppressWarnings("rawtypes")
	public List getNonGenericObjectList() {
		return nonGenericObjectList;
	}

	/**
	 * @param nonGenericObjectList
	 *            the nonGenericObjectList to set
	 */
	public void setNonGenericObjectList(@SuppressWarnings("rawtypes") List nonGenericObjectList) {
		this.nonGenericObjectList = nonGenericObjectList;
	}

	/**
	 * @return the myBirthdaysMap
	 */
	public Map<String, Calendar> getMyBirthdaysMap() {
		return myBirthdaysMap;
	}

	/**
	 * @param myBirthdaysMap
	 *            the myBirthdaysMap to set
	 */
	public void setMyBirthdaysMap(Map<String, Calendar> myBirthdaysMap) {
		this.myBirthdaysMap = myBirthdaysMap;
	}
}
