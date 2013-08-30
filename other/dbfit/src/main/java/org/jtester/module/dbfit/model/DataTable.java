package org.jtester.module.dbfit.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jtester.module.dbfit.exception.NoMatchingRowFoundException;

/**
 * vendor-invariant detached rowset implementation. Because oracle-specific
 * extensions effectively prevent us from using a generic cached result set,
 * this class plays that role instead. It is also responsible for efficient data
 * row matching and tracking processed/unprocessed rows.
 */
public class DataTable {
	private List<DataRow> rows = new LinkedList<DataRow>();
	private List<DataColumn> columns = new LinkedList<DataColumn>();

	public DataTable(ResultSet rs) throws Exception {
		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			columns.add(new DataColumn(rsmd, i));
		}
		while (rs.next()) {
			rows.add(new DataRow(rs, rsmd));
		}
	}

	public DataRow findMatching(Map<String, Object> keyProperties) throws NoMatchingRowFoundException {
		for (DataRow dr : rows) {
			boolean match = !dr.isProcessed() && dr.matches(keyProperties);
			if (match) {
				return dr;
			}
		}
		throw new NoMatchingRowFoundException();
	}

	public DataRow findFirstUnprocessedRow() throws NoMatchingRowFoundException {
		for (DataRow dr : rows) {
			boolean process = dr.isProcessed();
			if (process == false) {
				return dr;
			}
		}
		throw new NoMatchingRowFoundException();
	}

	public List<DataRow> getUnprocessedRows() {
		List<DataRow> l = new ArrayList<DataRow>();
		for (DataRow dr : rows) {
			boolean process = dr.isProcessed();
			if (process == false) {
				l.add(dr);
			}
		}
		return l;
	}

	public List<DataColumn> getColumns() {
		return columns;
	}

	/**
	 * 返回行数
	 * 
	 * @return
	 */
	public int getRowSize() {
		return this.rows.size();
	}

	/**
	 * 返回列数
	 * 
	 * @return
	 */
	public int getColSize() {
		return this.columns.size();
	}
}
