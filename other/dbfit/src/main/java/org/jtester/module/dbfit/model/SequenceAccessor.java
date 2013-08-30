package org.jtester.module.dbfit.model;

import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fit.Fixture;

public class SequenceAccessor extends DbParameterAccessor {

	public SequenceAccessor(DbParameterAccessor c, String sequence) {
		super(c.getName(), DbParameterAccessor.SEQUENCE, c.getSqlType(), c.type, c.getPosition());
		this.setPlaceholder(sequence);
	}

	private CallableStatement statement;
	private int index;

	public void bindTo(Fixture f, PreparedStatement cs, int ind) throws SQLException {
		this.statement = (CallableStatement) cs;
		this.fixture = f;
		this.index = ind;
		this.statement.registerOutParameter(ind, getSqlType());
	}

	public void set(Object value) throws Exception {
		throw new UnsupportedOperationException("Trying to set value of output parameter " + getName());
	}

	public Object get() throws IllegalAccessException, InvocationTargetException {
		try {
			Object o = statement.getObject(index);
			return normaliseValue(o);
		} catch (Exception e) {
			throw new InvocationTargetException(e);
		}
	}
}
