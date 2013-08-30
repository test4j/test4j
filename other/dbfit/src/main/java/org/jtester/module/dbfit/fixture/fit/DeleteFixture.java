package org.jtester.module.dbfit.fixture.fit;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.jtester.module.database.utility.DBHelper;
import org.jtester.module.dbfit.environment.DbFitEnvironment;
import org.jtester.module.dbfit.exception.HasMarkedException;
import org.jtester.module.dbfit.model.DbParameterAccessor;

import fit.Parse;

public class DeleteFixture extends InsertFixture {

	public DeleteFixture(DbFitEnvironment env, String tableName) {
		super(env, tableName);
	}

	public void doRows(Parse rows) {
		if ((tableName == null || tableName.trim().length() == 0) && args.length > 0) {
			tableName = args[0];
		} else if (tableName == null) {
			tableName = rows.parts.text();
			rows = rows.more;
		}
		PreparedStatement statement = null;
		try {
			initParameters(rows.parts);// init parameters from the first row
			statement = buildDeleteCommand(tableName, accessors);
			Parse row = rows;
			while ((row = row.more) != null) {
				deleteRowData(statement, row);
				right(row);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			if (!(e instanceof HasMarkedException)) {
				exception(rows.parts, e);
			}
		} finally {
			DBHelper.closeStatement(statement);
			statement = null;
		}
	}

	public PreparedStatement buildDeleteCommand(String tableName, DbParameterAccessor[] accessors) throws SQLException {
		String delete = environment.buildDeleteCommand(tableName, accessors);
		PreparedStatement cs = (environment.supportsOuputOnInsert()) ? environment.connect().prepareCall(delete)
				: environment.connect().prepareStatement(delete, Statement.RETURN_GENERATED_KEYS);
		for (int i = 0; i < accessors.length; i++) {
			accessors[i].bindTo(this, cs, i + 1);
		}
		return cs;
	}

	protected void deleteRowData(PreparedStatement statement, Parse row) {
		Parse cell = row.parts;
		try {
			statement.clearParameters();
			for (int column = 0; column < accessors.length; column++, cell = cell.more) {
				int direction = accessors[column].getDirection();
				if (direction == DbParameterAccessor.INPUT) {
					columnBindings[column].doCell(this, cell);
				}
			}
			statement.execute();
		} catch (Throwable e) {
			exception(cell, e);
			throw new HasMarkedException(e);
		}
	}
}
