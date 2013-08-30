package org.jtester.fortest.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class ResourceLoader {
	private final static Logger log4j = Logger.getLogger(ResourceLoader.class);

	private List<String> users;

	private DataSource dataSource;

	public List<String> getUsers() {
		return users;
	}

	/**
	 * 加载资源
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		log4j.info("init db");
		users = new ArrayList<String>();
		Connection conn = dataSource.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select id from tdd_user");
		while (rs.next()) {
			String id = rs.getString("id");
			users.add(id);
		}
		rs.close();
		stmt.close();
		conn.close();
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
