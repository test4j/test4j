package org.jtester.spec.spring;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

	public List<String> getUserByName(String name) {
		List<String> users = new ArrayList<String>();
		users.add("user1");
		users.add("user2");
		return users;
	}
}
