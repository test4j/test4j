package org.test4j.module.spring.testedbeans.autowired;

import org.test4j.fortest.beans.User;
import org.test4j.module.core.utility.MessageHelper;

public class UserDaoImpl implements IUserDao {

	public void insertUser(User user) {
		MessageHelper.info("user dao insert");
	}
}
