package org.jtester.datafilling.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("rawtypes")
public class CollectionsPojo {

	private List<String> strList = new ArrayList<String>();

	private ArrayList<String> arrayListStr = new ArrayList<String>();

	private List<String> copyOnWriteList = new CopyOnWriteArrayList<String>();

	private Set<String> strSet = new HashSet<String>();

	private HashSet<String> hashSetStr = new HashSet<String>();

	private Collection<String> strCollection = new ArrayList<String>();

	private Map<String, OneDimensionalTestPojo> map = new HashMap<String, OneDimensionalTestPojo>();

	private HashMap<String, OneDimensionalTestPojo> hashMap = new HashMap<String, OneDimensionalTestPojo>();

	private ConcurrentMap<String, OneDimensionalTestPojo> concurrentHashMap = new ConcurrentHashMap<String, OneDimensionalTestPojo>();

	private ConcurrentHashMap<String, OneDimensionalTestPojo> concurrentHashMapImpl = new ConcurrentHashMap<String, OneDimensionalTestPojo>();

	private Queue<SimplePojoToTestSetters> queue;

	private List nonGenerifiedList;

	private Map nonGenerifiedMap;

	public List<String> getStrList() {
		return strList;
	}

	public ArrayList<String> getArrayListStr() {
		return arrayListStr;
	}

	public Set<String> getStrSet() {
		return strSet;
	}

	public HashSet<String> getHashSetStr() {
		return hashSetStr;
	}

	public Collection<String> getStrCollection() {
		return strCollection;
	}

	public Map<String, OneDimensionalTestPojo> getMap() {
		return map;
	}

	public void setStrList(List<String> strList) {
		this.strList = strList;
	}

	public void setArrayListStr(ArrayList<String> arrayListStr) {
		this.arrayListStr = arrayListStr;
	}

	public void setStrSet(Set<String> strSet) {
		this.strSet = strSet;
	}

	public void setHashSetStr(HashSet<String> hashSetStr) {
		this.hashSetStr = hashSetStr;
	}

	public void setStrCollection(Collection<String> strCollection) {
		this.strCollection = strCollection;
	}

	public void setMap(Map<String, OneDimensionalTestPojo> map) {
		this.map = map;
	}

	public List<String> getCopyOnWriteList() {
		return copyOnWriteList;
	}

	public void setCopyOnWriteList(List<String> copyOnWriteList) {
		this.copyOnWriteList = copyOnWriteList;
	}

	public HashMap<String, OneDimensionalTestPojo> getHashMap() {
		return hashMap;
	}

	public void setHashMap(HashMap<String, OneDimensionalTestPojo> hashMap) {
		this.hashMap = hashMap;
	}

	public ConcurrentMap<String, OneDimensionalTestPojo> getConcurrentHashMap() {
		return concurrentHashMap;
	}

	public void setConcurrentHashMap(ConcurrentMap<String, OneDimensionalTestPojo> concurrentHashMap) {
		this.concurrentHashMap = concurrentHashMap;
	}

	public ConcurrentHashMap<String, OneDimensionalTestPojo> getConcurrentHashMapImpl() {
		return concurrentHashMapImpl;
	}

	public void setConcurrentHashMapImpl(ConcurrentHashMap<String, OneDimensionalTestPojo> concurrentHashMapImpl) {
		this.concurrentHashMapImpl = concurrentHashMapImpl;
	}

	public Queue<SimplePojoToTestSetters> getQueue() {
		return queue;
	}

	public void setQueue(Queue<SimplePojoToTestSetters> queue) {
		this.queue = queue;
	}

	public List getNonGenerifiedList() {
		return nonGenerifiedList;
	}

	public void setNonGenerifiedList(List nonGenerifiedList) {
		this.nonGenerifiedList = nonGenerifiedList;
	}

	public Map getNonGenerifiedMap() {
		return nonGenerifiedMap;
	}

	public void setNonGenerifiedMap(Map nonGenerifiedMap) {
		this.nonGenerifiedMap = nonGenerifiedMap;
	}
}
