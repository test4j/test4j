package org.jtester.fortest.service;

import java.util.List;

import org.jtester.fortest.beans.User;

public interface UserAnotherDao {
	List<User> findUserByPostcode(String postcode);

	void insertUser(User user);

	List<User> findAllUser();

	int partialNotMock();

	void callCascadedDao();
}
