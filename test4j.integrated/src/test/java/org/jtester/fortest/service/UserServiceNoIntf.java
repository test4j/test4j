package org.jtester.fortest.service;

import java.util.List;

import org.jtester.fortest.beans.User;

public class UserServiceNoIntf {
	private UserDao userDao;

	@SuppressWarnings("unused")
	private UserAnotherDao userAnotherDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setUserAnotherDao(UserAnotherDao userAnotherDao) {
		this.userAnotherDao = userAnotherDao;
	}

	public double paySalary(String postcode) {
		List<User> users = this.userDao.findUserByPostcode(postcode);
		double total = 0.0d;
		if (users == null || users.size() == 0) {
			return total;
		}
		for (User user : users) {
			total += user.getSarary();
		}
		return total;
	}

	public void insertUser(User user) {
		this.userDao.insertUser(user);
	}

	public void set(User user) {

	}

	public void insertUserException(User user) throws Exception {
		// this.userDao.insertUser(user);
		throw new Exception("insert user exception!");
	}

	public void setMaxTime(long maxTime) {

	}
}
