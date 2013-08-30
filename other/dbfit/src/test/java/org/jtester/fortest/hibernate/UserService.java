package org.jtester.fortest.hibernate;

public interface UserService extends BaseService<User> {
	public String findAddress();

	public User getUser(int id);

	public void newUser(User user);
}
