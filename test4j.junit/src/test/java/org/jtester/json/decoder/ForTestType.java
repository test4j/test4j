package org.jtester.json.decoder;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.jtester.json.encoder.beans.test.User;

@SuppressWarnings("rawtypes")
public class ForTestType {

	List<Integer> intList;

	List<Long> longList;

	List<Short> shortList;

	List<Double> doubleList;

	List<Float> floatList;

	List<Boolean> booleanList;

	Boolean[] booleanArray;

	Boolean[][] booleanArrayArray;

	List<Integer>[] listArray;

	ArrayList<User> userList;

	ArrayList objectList;

	Object[] objects;

	User[] users;

	public static Type getType(String name) throws Exception {
		Field field = ForTestType.class.getDeclaredField(name);
		return field.getGenericType();
	}
}