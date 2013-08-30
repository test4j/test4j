package org.jtester.module.tracer.spring;

import java.util.List;

import org.jtester.fortest.beans.User;
import org.jtester.fortest.service.UserAnotherDao;

public final class FinalUserDao implements UserAnotherDao {

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

	public void callCascadedDao() {
	}
}
