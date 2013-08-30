package org.jtester.fortest.service;

import java.util.List;

import org.jtester.fortest.beans.User;

public interface UserService {
	public double paySalary(String postcode);

	public void insertUser(User user);

	public void insertUserWillException(User user) throws Exception;

	public void insertUserException(User user) throws Exception;

	public String getServiceName();

	public List<User> findAllUser();
}
