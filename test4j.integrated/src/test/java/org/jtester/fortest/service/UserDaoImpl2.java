package org.jtester.fortest.service;

import java.util.Arrays;
import java.util.List;

import org.test4j.fortest.beans.User;
import org.test4j.fortest.service.UserDao;

public class UserDaoImpl2 implements UserDao {

	public List<User> findUserByPostcode(String postcode) {
		User user = new User("aaa", "bbb");
		return Arrays.asList(user);
	}

	public void insertUser(User user) {
	}

	public List<User> findAllUser() {
		User user = new User("ccc", "ddd");
		return Arrays.asList(user);
	}

	public int partialNotMock() {
		return 102;
	}
}
