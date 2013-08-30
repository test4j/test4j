package org.jtester.fortest.service;

import java.sql.SQLException;
import java.util.List;

import org.jtester.fortest.beans.User;
import org.jtester.module.spring.annotations.SpringInitMethod;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class UserAnotherDaoImpl extends SqlMapClientDaoSupport implements UserAnotherDao {

	private NestedDao nestedDao;

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

	public void callCascadedDao() {
		this.nestedDao.sayHell("test");
	}

	public void setNestedDao(NestedDao nestedDao) {
		this.nestedDao = nestedDao;
	}

	int springinit = 0;

	@SpringInitMethod
	public void springinit() {
		springinit = 100;
	}

	public static class NestedDao {
		private CascadedDao cascadedDao;

		public void sayHell(String hello) {
			this.cascadedDao.sayHell(hello);
		}

		public void setCascadedDao(CascadedDao cascadedDao) {
			this.cascadedDao = cascadedDao;
		}
	}

	public static class CascadedDao {
		public void sayHell(String hell) {

		}
	}
}
