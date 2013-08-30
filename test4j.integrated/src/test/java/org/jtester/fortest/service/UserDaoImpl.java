package org.jtester.fortest.service;

import java.sql.SQLException;
import java.util.List;

import org.jtester.fortest.beans.User;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class UserDaoImpl extends SqlMapClientDaoSupport implements UserDao {

	public List<User> findUserByPostcode(String postcode) {
		try {
			List users = this.getSqlMapClient().queryForList("TDD_COMMON.query_users_by_postcode", postcode);
			return users;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void insertUser(User user) {
		try {
			this.getSqlMapClient().insert("TDD_COMMON.insert_user", user);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<User> findAllUser() {
		try {
			List users = this.getSqlMapClient().queryForList("TDD_COMMON.query_all_users");
			return users;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public int partialNotMock() {
		return 100;
	}
}
