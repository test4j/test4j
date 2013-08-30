package org.jtester.module.spring.testedbeans.autowired;

import org.jtester.fortest.beans.User;
import org.jtester.module.core.utility.MessageHelper;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserDao userDao;

	public void insertUser(User user) {
		MessageHelper.info("actual service insertUser executed!");
		userDao.insertUser(user);
	}
}
