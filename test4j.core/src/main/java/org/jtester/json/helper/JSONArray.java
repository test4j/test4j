package org.jtester.json.helper;

import java.util.LinkedList;
import java.util.List;

public class JSONArray extends LinkedList<JSONObject> implements JSONObject {
	private static final long serialVersionUID = 1L;

	public JSONArray() {
		super();
	}

	public JSONArray(List<JSONObject> list) {
		super(list);
	}

	/**
	 * same as add(JSON json)
	 * 
	 * @param o
	 */
	public void addJSON(Object o) {
		JSONObject newo = JSONSingle.convertJSON(o);
		super.add(newo);
	}

	public String description() {
		return toString();
	}
}
