package org.jtester.module.dbfit.fixture.fit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jtester.module.database.utility.DBHelper;
import org.jtester.module.dbfit.environment.DbFitEnvironment;
import org.jtester.module.dbfit.fixture.JTesterFixture;
import org.jtester.module.dbfit.model.DataTable;

import fit.Parse;

public class StoreQueryTableFixture extends JTesterFixture {
	private DbFitEnvironment dbEnvironment;
	private String query;
	private String symbolName;

	public StoreQueryTableFixture(DbFitEnvironment environment, String query, String symbolName) {
		this.dbEnvironment = environment;
		this.query = query;
		this.symbolName = symbolName;
	}

	public void doTable(Parse table) {
		if (query == null || symbolName == null) {
			if (args.length < 2) {
				String err = "No query and symbol name specified to StoreQuery constructor or argument list";
				throw new UnsupportedOperationException(err);
			}
			query = args[0];
			symbolName = args[1];
		}
		if (symbolName.startsWith(">>")) {
			symbolName = symbolName.substring(2);
		}
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = dbEnvironment.createStatementWithBoundFixtureSymbols(query);
			rs = st.executeQuery();
			DataTable dt = new DataTable(rs);
			org.jtester.module.dbfit.utility.SymbolUtil.setSymbol(symbolName, dt);
		} catch (Exception sqle) {
			throw new Error(sqle);
		} finally {
			DBHelper.closeResultSet(rs);
			rs = null;
			DBHelper.closeStatement(st);
			st = null;
		}
	}
}