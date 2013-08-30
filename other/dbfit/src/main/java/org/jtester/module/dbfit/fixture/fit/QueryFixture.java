package org.jtester.module.dbfit.fixture.fit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jtester.module.core.utility.MessageHelper;
import org.jtester.module.database.utility.DBHelper;
import org.jtester.module.dbfit.environment.DbFitEnvironment;
import org.jtester.module.dbfit.model.DataColumn;
import org.jtester.module.dbfit.model.DataTable;
import org.jtester.module.dbfit.utility.SymbolUtil;

@SuppressWarnings({ "rawtypes" })
public class QueryFixture extends RowSetFixture {

	private DbFitEnvironment dbEnvironment;
	private String query;
	private boolean isOrdered;

	public QueryFixture(DbFitEnvironment environment, String query) {
		this(environment, query, false);
	}

	public QueryFixture(DbFitEnvironment environment, String query, boolean isOrdered) {
		this.dbEnvironment = environment;
		this.query = query;
		this.isOrdered = isOrdered;
	}

	public DataTable getDataTable() throws Exception {
		if (query == null) {
			query = args[0];
		}
		if (query.startsWith("<<")) {
			return getFromSymbol();
		}
		MessageHelper.info(String.format("Query: '%s'", query));
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = dbEnvironment.createStatementWithBoundFixtureSymbols(query);
			rs = st.executeQuery();
			DataTable dt = new DataTable(rs);
			return dt;
		} finally {
			DBHelper.closeResultSet(rs);
			rs = null;
			DBHelper.closeStatement(st);
			st = null;
		}
	}

	private DataTable getFromSymbol() throws Exception {
		Object o = SymbolUtil.getSymbol(query.substring(2).trim());
		if (o instanceof DataTable) {
			return (DataTable) o;
		}
		ResultSet rs = null;

		if (o instanceof ResultSet) {
			rs = (ResultSet) o;
		} else {
			String err = "Stored queries can only be used on symbols that contain result sets";
			throw new UnsupportedOperationException(err);
		}
		try {
			DataTable dt = new DataTable(rs);
			return dt;
		} finally {
			DBHelper.closeResultSet(rs);
			rs = null;
		}
	}

	protected boolean isOrdered() {
		return isOrdered;
	}

	@Override
	protected Class getJavaClassForColumn(DataColumn col) {
		return dbEnvironment.getJavaClass(col.getDbTypeName());
	}
}
