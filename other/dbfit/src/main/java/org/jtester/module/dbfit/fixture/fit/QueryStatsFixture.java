package org.jtester.module.dbfit.fixture.fit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jtester.module.database.utility.DBHelper;
import org.jtester.module.dbfit.environment.DbFitEnvironment;

import fit.ColumnFixture;

public class QueryStatsFixture extends ColumnFixture {
	private DbFitEnvironment environment;

	public QueryStatsFixture(DbFitEnvironment environment) {
		this.environment = environment;
	}

	public String tableName;
	public String where;
	public String query;

	public void setViewName(String value) {
		tableName = value;
	}

	private boolean hasExecuted = false;

	public void reset() {
		hasExecuted = false;
		where = null;
		query = null;
		_rows = 0;
		tableName = null;
	}

	private int _rows;

	private void execQuery() throws SQLException {
		if (hasExecuted) {
			return;
		}
		if (query == null) {
			query = "select * from " + tableName + (where != null ? " where " + where : "");
		}
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = environment.createStatementWithBoundFixtureSymbols("select count(*) from (" + query + ") temp");
			rs = st.executeQuery();
			if (rs.next()) {
				_rows = rs.getInt(1);
			}
			hasExecuted = true;
		} finally {
			DBHelper.closeResultSet(rs);
			rs = null;
			DBHelper.closeStatement(st);
			st = null;
		}
	}

	public int rowCount() throws SQLException {
		execQuery();
		return _rows;
	}

	public boolean isEmpty() throws SQLException {
		return rowCount() == 0;
	}
}
