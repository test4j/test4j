package org.test4j.module.dbfit.fixture.fit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.test4j.module.database.utility.DBHelper;
import org.test4j.module.dbfit.environment.DbFitEnvironment;
import org.test4j.module.dbfit.fixture.Test4JFixture;
import org.test4j.module.dbfit.model.DataTable;

import fit.Parse;

public class StoreQueryTableFixture extends Test4JFixture {
    private DbFitEnvironment dbEnvironment;
    private String           query;
    private String           symbolName;

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
            org.test4j.module.dbfit.utility.SymbolUtil.setSymbol(symbolName, dt);
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
