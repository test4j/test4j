package org.jtester.module.dbfit.fixture.fit;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jtester.module.database.environment.normalise.NameNormaliser;
import org.jtester.module.database.utility.DBHelper;
import org.jtester.module.dbfit.environment.DbFitEnvironment;
import org.jtester.module.dbfit.exception.NoMatchingRowFoundException;
import org.jtester.module.dbfit.fixture.JTesterFixture;
import org.jtester.module.dbfit.model.DataRow;
import org.jtester.module.dbfit.model.DataTable;

import fit.Fixture;
import fit.Parse;

public class CompareStoredQueriesFixture extends JTesterFixture {
	private String symbol1;
	private String symbol2;
	private DataTable dt1;
	private DataTable dt2;
	private String[] columnNames;
	private boolean[] keyProperties;

	public CompareStoredQueriesFixture() {
	}

	public CompareStoredQueriesFixture(DbFitEnvironment environment, String symbol1, String symbol2) {
		this.symbol1 = symbol1;
		this.symbol2 = symbol2;
	}

	private DataTable getDataTable(String symbolName) {
		Object o = org.jtester.module.dbfit.utility.SymbolUtil.getSymbol(symbolName);
		if (o == null) {
			throw new UnsupportedOperationException("Cannot load a stored query from " + symbolName);
		}
		if (o instanceof DataTable) {
			return (DataTable) o;
		}
		ResultSet rs = null;
		if (o instanceof ResultSet) {
			rs = (ResultSet) o;
		} else {
			throw new UnsupportedOperationException("Cannot load stored query from " + symbolName
					+ " - object type is " + o.getClass().getName());
		}
		try {
			DataTable dt = new DataTable(rs);
			return dt;
		} catch (Exception e) {
			throw new UnsupportedOperationException("Cannot load stored query from " + symbolName, e);
		} finally {
			DBHelper.closeResultSet(rs);
			rs = null;
		}
	}

	private void initialiseDataTables() {
		if (symbol1 == null || symbol2 == null) {
			if (args.length < 2)
				throw new UnsupportedOperationException(
						"No symbols specified to CompareStoreQueries constructor or argument list");
			symbol1 = args[0];
			symbol2 = args[1];
		}
		if (symbol1.startsWith("<<"))
			symbol1 = symbol1.substring(2);
		if (symbol2.startsWith("<<"))
			symbol2 = symbol2.substring(2);
		dt1 = getDataTable(symbol1);
		dt2 = getDataTable(symbol2);
	}

	public void doTable(Parse table) {
		initialiseDataTables();
		Parse lastRow = table.parts.more;
		if (lastRow == null) {
			throw new Error("Query structure missing from second row");
		}
		loadRowStructure(lastRow);
		lastRow = processDataTable(dt1, dt2, lastRow, symbol2);

		List<DataRow> unproc = dt2.getUnprocessedRows();
		for (DataRow dr : unproc) {
			lastRow = addRow(lastRow, dr, true, " missing from " + symbol1);
		}
	}

	private void loadRowStructure(Parse headerRow) {
		Parse headerCell = headerRow.parts;
		int colNum = headerRow.parts.size();
		columnNames = new String[colNum];
		keyProperties = new boolean[colNum];
		for (int i = 0; i < colNum; i++) {
			String currentName = headerCell.text();
			if (currentName == null)
				throw new UnsupportedOperationException("Column " + i + " does not have a name");
			currentName = currentName.trim();
			if (currentName.length() == 0)
				throw new UnsupportedOperationException("Column " + i + " does not have a name");
			columnNames[i] = NameNormaliser.normaliseName(currentName);
			keyProperties[i] = !currentName.endsWith("?");
			headerCell = headerCell.more;
		}
	}

	private Parse processDataTable(DataTable t1, DataTable t2, Parse lastScreenRow, String queryName) {

		List<DataRow> unproc = t1.getUnprocessedRows();
		for (DataRow dr : unproc) {
			Map<String, Object> matchingMask = new HashMap<String, Object>();
			for (int i = 0; i < keyProperties.length; i++) {
				if (keyProperties[i])
					matchingMask.put(columnNames[i], dr.get(columnNames[i]));
			}
			try {
				DataRow dr2 = t2.findMatching(matchingMask);
				dr2.markProcessed();
				lastScreenRow = addRow(lastScreenRow, dr, dr2);
			} catch (NoMatchingRowFoundException nex) {
				lastScreenRow = addRow(lastScreenRow, dr, true, " missing from " + queryName);
			}
			dr.markProcessed();
		}
		return lastScreenRow;
	}

	private Parse addRow(Parse lastRow, DataRow dr, DataRow dr2) {
		Parse newRow = new Parse("tr", null, null, null);
		lastRow.more = newRow;
		lastRow = newRow;
		try {
			String lval = dr.getStringValue(columnNames[0]);
			String rval = dr2.getStringValue(columnNames[0]);
			Parse firstCell = new Parse("td", lval, null, null);
			newRow.parts = firstCell;
			if (!lval.equals(rval)) {
				wrong(firstCell, rval);
			} else {
				right(firstCell);
			}
			for (int i = 1; i < columnNames.length; i++) {
				lval = dr.getStringValue(columnNames[i]);
				rval = dr2.getStringValue(columnNames[i]);
				Parse nextCell = new Parse("td", lval, null, null);
				firstCell.more = nextCell;
				firstCell = nextCell;
				if (!lval.equals(rval)) {
					wrong(firstCell, rval);
				} else {
					right(firstCell);
				}
			}
		} catch (Throwable e) {
			exception(newRow, e);
		}
		return lastRow;
	}

	private Parse addRow(Parse lastRow, DataRow dr, boolean markAsError, String desc) {
		Parse newRow = new Parse("tr", null, null, null);
		lastRow.more = newRow;
		lastRow = newRow;
		try {
			Parse firstCell = new Parse("td", dr.getStringValue(columnNames[0]), null, null);
			newRow.parts = firstCell;
			if (markAsError) {
				firstCell.addToBody(Fixture.gray(desc));
				wrong(firstCell);
			}
			for (int i = 1; i < columnNames.length; i++) {
				Parse nextCell = new Parse("td", dr.getStringValue(columnNames[i]), null, null);
				firstCell.more = nextCell;
				firstCell = nextCell;
			}
		} catch (Throwable e) {
			exception(newRow, e);
		}
		return lastRow;
	}
}
