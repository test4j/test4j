package org.jtester.module.dbfit.fixture.fit;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.jtester.module.database.utility.DBHelper;
import org.jtester.module.dbfit.environment.DbFitEnvironment;

import fit.ColumnFixture;
import fit.Parse;

public class CleanFixture extends ColumnFixture {
	private DbFitEnvironment environment;

	public CleanFixture(DbFitEnvironment environment) {
		this.environment = environment;
	}

	public String table;
	public String columnName;
	public BigDecimal[] ids;
	public String[] keys;
	public String where = null;

	private String getIDCSV() {
		StringBuilder sb = new StringBuilder();
		String comma = "";
		for (BigDecimal x : ids) {
			sb.append(comma);
			sb.append(x.toString());
			comma = ", ";
		}
		return sb.toString();
	}

	private String getKeyCSV() {
		StringBuilder sb = new StringBuilder();
		String comma = "";
		for (String x : keys) {
			sb.append(comma);
			sb.append("'");
			sb.append(x.toString());
			sb.append("'");
			comma = ", ";
		}
		return sb.toString();
	}

	private boolean hadRowOperation = false;

	public boolean clean() throws SQLException {
		String s = "Delete from " + table + (where != null ? (" where " + where) : "");
		PreparedStatement statement = null;
		try {
			statement = environment.createStatementWithBoundFixtureSymbols(s);
			statement.execute();
			return true;
		} finally {
			DBHelper.closeStatement(statement);
			statement = null;
		}
	}

	public boolean DeleteRowsForIDs() throws SQLException {
		String s = "Delete from " + table + " where " + columnName + " in (" + getIDCSV() + ") "
				+ (where != null ? " and " + where : "");
		PreparedStatement statement = null;
		try {
			statement = environment.createStatementWithBoundFixtureSymbols(s);
			statement.execute();
			hadRowOperation = true;
			return true;
		} finally {
			DBHelper.closeStatement(statement);
			statement = null;
		}
	}

	public boolean DeleteRowsForKeys() throws SQLException {
		String s = "Delete from " + table + " where " + columnName + " in (" + getKeyCSV() + ") "
				+ (where != null ? " and " + where : "");
		PreparedStatement statement = null;
		try {
			statement = environment.createStatementWithBoundFixtureSymbols(s);
			statement.execute();
			hadRowOperation = true;
			return true;
		} finally {
			DBHelper.closeStatement(statement);
			statement = null;
		}
	}

	public void doRow(Parse row) {
		hadRowOperation = false;
		super.doRow(row);
		if (!hadRowOperation) {
			try {
				clean();
			} catch (SQLException sqle) {
				exception(row, sqle);
			}
		}
	}
}
