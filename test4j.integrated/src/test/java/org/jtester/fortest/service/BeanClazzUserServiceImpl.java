package org.jtester.fortest.service;

import java.util.List;

import org.test4j.fortest.beans.User;
import org.test4j.fortest.service.UserAnotherDao;
import org.test4j.fortest.service.UserDao;
import org.test4j.fortest.service.UserService;

@SuppressWarnings("unused")
public class BeanClazzUserServiceImpl implements UserService {
	private UserDao userDao;

	private UserAnotherDao userAnotherDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setUserAnotherDao(UserAnotherDao userAnotherDao) {
		this.userAnotherDao = userAnotherDao;
	}

	public String getServiceName() {
		return "BeanClazzUserServiceImpl";
	}

	public void insertUser(User user) {
	}

	public void insertUserException(User user) throws Exception {
	}

	public double paySalary(String postcode) {
		return 0;
	}

	public List<User> findAllUser() {
		return this.userDao.findAllUser();
	}

	public void insertUserWillException(User user) throws Exception {

	}
}
