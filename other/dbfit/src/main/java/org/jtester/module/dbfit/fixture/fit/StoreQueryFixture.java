package org.test4j.module.dbfit.fixture.fit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.test4j.module.database.utility.DBHelper;
import org.test4j.module.dbfit.environment.DbFitEnvironment;
import org.test4j.module.dbfit.fixture.Test4JFixture;
import org.test4j.module.dbfit.model.DataRow;
import org.test4j.module.dbfit.model.DataTable;

import fit.Parse;

public class StoreQueryFixture extends Test4JFixture {
    private DbFitEnvironment dbEnvironment;
    private String           query;
    private String           symbolName;

    public StoreQueryFixture(DbFitEnvironment environment, String query, String symbolName) {
        this.dbEnvironment = environment;
        this.query = query;
        this.symbolName = symbolName;
    }

    public void doTable(Parse table) {
        if (query == null || symbolName == null) {
            if (args.length < 2)
                throw new UnsupportedOperationException(
                        "No query and symbol name specified to StoreQuery constructor or argument list");
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

            int rowSize = dt.getRowSize();
            if (rowSize == 0) {
                throw new RuntimeException("can't retrieve any data by query:" + query);
            } else if (rowSize > 1) {
                throw new RuntimeException("allowing one row data retrieved by query:" + query);
            }
            int colSize = dt.getColSize();
            if (colSize == 0) {
                throw new RuntimeException("can't retrieve any column by query:" + query);
            } else if (colSize > 1) {
                throw new RuntimeException("allowing one column data retrieved by query:" + query);
            }
            DataRow row = dt.getUnprocessedRows().get(0);
            List<String> values = row.getStringValues();
            org.test4j.module.dbfit.utility.SymbolUtil.setSymbol(symbolName, values.get(0));
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
