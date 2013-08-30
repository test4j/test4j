package org.jtester.module.spring.testedbeans.autowired;

import org.jtester.fortest.beans.User;
import org.jtester.module.core.utility.MessageHelper;

public class UserDaoImpl implements IUserDao {

	public void insertUser(User user) {
		MessageHelper.info("user dao insert");
	}
}
