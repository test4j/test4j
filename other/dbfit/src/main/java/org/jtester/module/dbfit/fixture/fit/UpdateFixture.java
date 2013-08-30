package org.jtester.module.dbfit.fixture.fit;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jtester.module.database.environment.normalise.NameNormaliser;
import org.jtester.module.database.utility.DBHelper;
import org.jtester.module.dbfit.environment.DbFitEnvironment;
import org.jtester.module.dbfit.exception.HasMarkedException;
import org.jtester.module.dbfit.model.DbParameterAccessor;
import org.jtester.module.dbfit.model.SymbolAccessSetBinding;

import fit.Binding;
import fit.Fixture;
import fit.Parse;

public class UpdateFixture extends Fixture {
	private DbFitEnvironment environment;
	private String tableName;
	private Binding[] columnBindings;
	private DbParameterAccessor[] updateAccessors;
	private DbParameterAccessor[] selectAccessors;

	public UpdateFixture(DbFitEnvironment dbEnvironment) {
		this.environment = dbEnvironment;
	}

	public UpdateFixture(DbFitEnvironment dbEnvironment, String tableName) {
		this.tableName = tableName;
		this.environment = dbEnvironment;
	}

	public PreparedStatement buildUpdateCommand() throws SQLException {
		if (updateAccessors.length == 0) {
			String err = "Update fixture must have at least one field to update. Have you forgotten = after the column name?";
			throw new Error(err);
		}
		StringBuilder s = new StringBuilder("update ").append(tableName).append(" set ");
		for (int i = 0; i < updateAccessors.length; i++) {
			if (i > 0) {
				s.append(", ");
			}
			s.append(updateAccessors[i].getName()).append("=").append("?");
		}
		s.append(" where ");
		for (int i = 0; i < selectAccessors.length; i++) {
			if (i > 0) {
				s.append(" and ");
			}
			s.append(selectAccessors[i].getName()).append("=").append("?");
		}

		PreparedStatement cs = environment.connect().prepareStatement(s.toString());
		for (int i = 0; i < updateAccessors.length; i++) {
			updateAccessors[i].bindTo(this, cs, i + 1);
		}
		for (int j = 0; j < selectAccessors.length; j++) {
			selectAccessors[j].bindTo(this, cs, j + updateAccessors.length + 1);
		}
		return cs;
	}

	public void doRows(Parse rows) {
		// if table not defined as parameter, read from fixture argument; if
		// still not defined, read from first row
		if ((tableName == null || tableName.trim().length() == 0) && args.length > 0) {
			tableName = args[0];
		} else if (tableName == null) {
			tableName = rows.parts.text();
			rows = rows.more;
		}
		PreparedStatement statement = null;
		try {
			initParameters(rows.parts);// init parameters from the first row
			statement = buildUpdateCommand();
			Parse row = rows;
			while ((row = row.more) != null) {
				runRow(statement, row);
			}
		} catch (Throwable e) {
			exception(rows.parts, e);
		} finally {
			DBHelper.closeStatement(statement);
			statement = null;
		}
	}

	private void initParameters(Parse headerCells) throws SQLException {
		Map<String, DbParameterAccessor> allParams = environment.getAllColumns(tableName);
		if (allParams.isEmpty()) {
			throw new SQLException("Cannot retrieve list of columns for " + tableName
					+ " - check spelling and access rights");
		}
		columnBindings = new Binding[headerCells.size()];
		List<DbParameterAccessor> selectAcc = new ArrayList<DbParameterAccessor>();
		List<DbParameterAccessor> updateAcc = new ArrayList<DbParameterAccessor>();

		for (int i = 0; headerCells != null; i++, headerCells = headerCells.more) {
			String name = headerCells.text();
			String paramName = NameNormaliser.normaliseName(name);
			// need to clone db param accessors here because same column may be
			// in the update and select part
			DbParameterAccessor orig = allParams.get(paramName);
			if (orig == null) {
				wrong(headerCells);
				throw new SQLException("Cannot find column " + paramName);
			}
			// clone parameter because there may be multiple usages of the same
			// column
			DbParameterAccessor acc = new DbParameterAccessor(orig);
			acc.setDirection(DbParameterAccessor.INPUT);
			if (headerCells.text().endsWith("="))
				updateAcc.add(acc);
			else
				selectAcc.add(acc);
			columnBindings[i] = new SymbolAccessSetBinding();
			columnBindings[i].adapter = acc;
		}
		// weird jdk syntax, method param is the type of array.
		selectAccessors = selectAcc.toArray(new DbParameterAccessor[0]);
		updateAccessors = updateAcc.toArray(new DbParameterAccessor[0]);
	}

	private void runRow(PreparedStatement statement, Parse row) throws Exception {
		Parse cell = row.parts;
		try {
			statement.clearParameters();
			// first set input params
			for (int column = 0; column < columnBindings.length; column++, cell = cell.more) {
				columnBindings[column].doCell(this, cell);
			}
			statement.execute();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			exception(row, sqle);
			row.parts.last().more = new Parse("td", sqle.getMessage(), null, null);
		} catch (Throwable e) {
			exception(cell, e);
			throw new HasMarkedException(e);
		}
	}
}
