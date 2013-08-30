package org.jtester.module.spring.testedbeans.resource;

import java.util.List;

import javax.annotation.Resource;

import org.jtester.fortest.beans.User;
import org.jtester.fortest.service.UserDao;
import org.jtester.fortest.service.UserService;

public class UserServiceResourceImpl implements UserService {

	@Resource
	private UserDao userDao;

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

	public void insertUserWillException(User user) throws Exception {
		this.userDao.insertUser(user);
		throw new Exception("insert user exception!");
	}

	public void insertUserException(User user) throws Exception {
		throw new Exception("insert user exception!");
	}

	public String getServiceName() {
		return "org.jtester.module.spring.resource.UserServiceResourceImpl";
	}

	public List<User> findAllUser() {
		return this.userDao.findAllUser();
	}
}
