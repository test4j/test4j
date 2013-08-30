package org.jtester.module.spring.testedbeans.resource;

import java.util.List;

import org.jtester.fortest.beans.User;
import org.jtester.fortest.service.UserDao;

public class UserDaoResourceImpl implements UserDao {

	public List<User> findUserByPostcode(String postcode) {
		return null;
	}

	public void insertUser(User user) {
	}

	public List<User> findAllUser() {
		return null;
	}

	public int partialNotMock() {
		return 0;
	}

}
