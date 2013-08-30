package org.jtester.module.dbfit.environment;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import oracle.jdbc.rowset.OracleCachedRowSet;

import org.jtester.module.database.environment.DBEnvironment;
import org.jtester.module.database.environment.normalise.NameNormaliser;
import org.jtester.module.database.environment.normalise.TypeNormaliser;
import org.jtester.module.database.environment.normalise.TypeNormaliserFactory;
import org.jtester.module.database.environment.typesmap.OracleTypeMap;
import org.jtester.module.database.utility.DBHelper;
import org.jtester.module.dbfit.model.DbParameterAccessor;
import org.jtester.module.dbfit.model.OracleBlobTypeAdapter;
import org.jtester.module.dbfit.model.SqlTimestampParseDelegate;
import org.jtester.tools.commons.ResourceHelper;

import ext.jtester.apache.commons.io.IOUtils;
import fit.TypeAdapter;

@SuppressWarnings({ "rawtypes" })
public class DbFitOracleEnvironment extends DbFitAbstractDBEnvironment {
	public static class OracleTimestampParser {
		public static Object parse(String s) throws Exception {
			return new oracle.sql.TIMESTAMP((java.sql.Timestamp) SqlTimestampParseDelegate.parse(s));
		}
	}

	public static class OracleTimestampNormaliser implements TypeNormaliser {
		public Object normalise(Object o) throws SQLException {
			if (o == null)
				return null;
			if (!(o instanceof oracle.sql.TIMESTAMP)) {
				throw new UnsupportedOperationException("OracleTimestampNormaliser cannot work with " + o.getClass());
			}
			oracle.sql.TIMESTAMP ts = (oracle.sql.TIMESTAMP) o;
			return ts.timestampValue();
		}
	}

	public static class OracleDateNormaliser implements TypeNormaliser {
		public Object normalise(Object o) throws SQLException {
			if (o == null)
				return null;
			if (!(o instanceof oracle.sql.DATE)) {
				throw new UnsupportedOperationException("OracleDateNormaliser cannot work with " + o.getClass());
			}
			oracle.sql.DATE ts = (oracle.sql.DATE) o;
			return ts.timestampValue();
		}
	}

	// transparently convert outcoming sql date into sql timestamps
	public static class SqlDateNormaliser implements TypeNormaliser {
		public Object normalise(Object o) throws SQLException {
			if (o == null)
				return null;
			if (!(o instanceof java.sql.Date)) {
				throw new UnsupportedOperationException("SqlDateNormaliser cannot work with " + o.getClass());
			}
			java.sql.Date ts = (java.sql.Date) o;
			return new java.sql.Timestamp(ts.getTime());
		}
	}

	public static class OracleClobNormaliser implements TypeNormaliser {
		private static final int MAX_CLOB_LENGTH = 10000;

		public Object normalise(Object o) throws SQLException {
			if (o == null)
				return null;
			if (!(o instanceof oracle.sql.CLOB)) {
				throw new UnsupportedOperationException("OracleClobNormaliser cannot work with " + o.getClass());
			}
			oracle.sql.CLOB clob = (oracle.sql.CLOB) o;
			if (clob.length() > MAX_CLOB_LENGTH)
				throw new UnsupportedOperationException("Clobs larger than " + MAX_CLOB_LENGTH
						+ "bytes are not supported by DBFIT");
			InputStream is = clob.getStream();
			String value = ResourceHelper.readFromStream(is);
			return value;
		}
	}

	public static class OracleBlobNormaliser implements TypeNormaliser {
		private static final int MAX_CLOB_LENGTH = 10000;

		public Object normalise(Object o) throws Exception {
			if (o == null)
				return null;
			if (!(o instanceof oracle.sql.BLOB)) {
				throw new UnsupportedOperationException("OracleClobNormaliser cannot work with " + o.getClass());
			}
			oracle.sql.BLOB blob = (oracle.sql.BLOB) o;
			if (blob.length() > MAX_CLOB_LENGTH)
				throw new UnsupportedOperationException("Clobs larger than " + MAX_CLOB_LENGTH
						+ "bytes are not supported by DBFIT");
			char[] base64 = IOUtils.toCharArray(blob.getBinaryStream());
			String value = new String(base64);
			return value;
		}
	}

	public static class OracleRefNormaliser implements TypeNormaliser {
		public Object normalise(Object o) throws SQLException {
			if (o == null) {
				return null;
			}
			ResultSet rs = null;
			try {
				if (o instanceof ResultSet) {
					rs = (ResultSet) o;
					OracleCachedRowSet ocrs = new OracleCachedRowSet();
					ocrs.populate(rs);
					return ocrs;
				} else {
					String err = "OracleRefNormaliser cannot work on " + o.getClass();
					throw new UnsupportedOperationException(err);
				}
			} finally {
				DBHelper.closeResultSet(rs);
				rs = null;
			}
		}
	}

	public DbFitOracleEnvironment(DBEnvironment dbEnvironment) {
		super(dbEnvironment);
		TypeNormaliserFactory.setNormaliser(oracle.sql.TIMESTAMP.class, new OracleTimestampNormaliser());
		TypeNormaliserFactory.setNormaliser(oracle.sql.DATE.class, new OracleDateNormaliser());
		TypeNormaliserFactory.setNormaliser(oracle.sql.CLOB.class, new OracleClobNormaliser());
		TypeNormaliserFactory.setNormaliser(oracle.sql.BLOB.class, new OracleBlobNormaliser());

		TypeNormaliserFactory.setNormaliser(java.sql.Date.class, new SqlDateNormaliser());

		TypeAdapter.registerParseDelegate(oracle.sql.BLOB.class, OracleBlobTypeAdapter.class);
		try {
			TypeNormaliserFactory.setNormaliser(Class.forName("oracle.jdbc.driver.OracleResultSetImpl"),
					new OracleRefNormaliser());
		} catch (Throwable e) {
			throw new Error("Cannot initialise oracle rowset", e);
		}
	}

	public boolean supportsOuputOnInsert() {
		return true;
	}

	private static Pattern paramsNames = Pattern.compile(":([A-Za-z0-9_]+)");

	public Pattern getParameterPattern() {
		return paramsNames;
	}

	public Map<String, DbParameterAccessor> getAllProcedureParameters(String procName) throws SQLException {
		String[] qualifiers = NameNormaliser.normaliseName(procName).split("\\.");
		String cols = " argument_name, data_type, data_length,  IN_OUT, sequence ";
		String qry = "select " + cols + "  from all_arguments where data_level=0 and ";
		if (qualifiers.length == 3) {
			qry += " owner=? and package_name=? and object_name=? ";
		} else if (qualifiers.length == 2) {
			qry += " ((owner=? and package_name is null and object_name=?) or "
					+ " (owner=user and package_name=? and object_name=?))";
		} else {
			qry += " (owner=user and package_name is null and object_name=?)";
		}

		// map to public synonyms also
		if (qualifiers.length < 3) {
			qry += " union all " + " select " + cols + " from all_arguments, all_synonyms "
					+ " where data_level=0 and all_synonyms.owner='PUBLIC' and all_arguments.owner=table_owner and ";
			if (qualifiers.length == 2) { // package
				qry += " package_name=table_name and synonym_name=? and object_name=? ";
			} else {
				qry += " package_name is null and object_name=table_name and synonym_name=? ";
			}
		}
		qry += " order by sequence ";
		if (qualifiers.length == 2) {
			String[] newQualifiers = new String[6];
			newQualifiers[0] = qualifiers[0];
			newQualifiers[1] = qualifiers[1];
			newQualifiers[2] = qualifiers[0];
			newQualifiers[3] = qualifiers[1];
			newQualifiers[4] = qualifiers[0];
			newQualifiers[5] = qualifiers[1];
			qualifiers = newQualifiers;
		} else if (qualifiers.length == 1) {
			String[] newQualifiers = new String[2];
			newQualifiers[0] = qualifiers[0];
			newQualifiers[1] = qualifiers[0];
			qualifiers = newQualifiers;
		}
		return readIntoParams(qualifiers, qry);
	}

	public Map<String, DbParameterAccessor> getAllColumns(String tableOrViewName) throws SQLException {
		String[] qualifiers = NameNormaliser.normaliseName(tableOrViewName).split("\\.");
		String qry = " select column_name, data_type, data_length, "
				+ " 'IN' as direction, column_id from all_tab_columns where ";
		if (qualifiers.length == 2) {
			qry += " owner=? and table_name=? ";
		} else {
			qry += " (owner=user and table_name=?)";
		}
		qry += " order by column_id ";
		return readIntoParams(qualifiers, qry);
	}

	private Map<String, DbParameterAccessor> readIntoParams(String[] queryParameters, String query) throws SQLException {
		CallableStatement dc = null;
		ResultSet rs = null;
		try {
			dc = this.connect().prepareCall(query);
			for (int i = 0; i < queryParameters.length; i++) {
				dc.setString(i + 1, queryParameters[i].toUpperCase());
			}
			rs = dc.executeQuery();
			Map<String, DbParameterAccessor> allParams = new HashMap<String, DbParameterAccessor>();
			int position = 0;
			while (rs.next()) {
				String paramName = rs.getString(1);
				if (paramName == null)
					paramName = "";
				String dataType = rs.getString(2);
				// int length = rs.getInt(3);
				String direction = rs.getString(4);
				int paramDirection;
				if (paramName.trim().length() == 0) {
					paramDirection = DbParameterAccessor.RETURN_VALUE;
				} else {
					paramDirection = getParameterDirection(direction);
				}

				DbParameterAccessor dbp = new DbParameterAccessor(paramName, paramDirection, getSqlType(dataType),
						getJavaClass(dataType), paramDirection == DbParameterAccessor.RETURN_VALUE ? -1 : position++);
				allParams.put(NameNormaliser.normaliseName(paramName), dbp);
			}
			return allParams;
		} finally {
			DBHelper.closeResultSet(rs);
			rs = null;
			DBHelper.closeStatement(dc);
			dc = null;
		}
	}

	private static String normaliseTypeName(String dataType) {
		dataType = dataType.toUpperCase().trim();
		int idx = dataType.indexOf(" ");
		if (idx >= 0)
			dataType = dataType.substring(0, idx);
		idx = dataType.indexOf("(");
		if (idx >= 0)
			dataType = dataType.substring(0, idx);
		return dataType;
	}

	private static int getSqlType(String dataType) {
		dataType = normaliseTypeName(dataType);
		Integer type = OracleTypeMap.getSQLType(dataType);
		if (type != null) {
			return type;
		} else {
			throw new UnsupportedOperationException("Type " + dataType + " is not supported");
		}
	}

	public Class getJavaClass(String dataType) {
		dataType = normaliseTypeName(dataType);
		Class clazz = OracleTypeMap.getJavaType(dataType);
		if (clazz != null) {
			return clazz;
		} else {
			throw new UnsupportedOperationException("Type " + dataType + " is not supported");
		}
	}

	private static int getParameterDirection(String direction) {
		if ("IN".equals(direction))
			return DbParameterAccessor.INPUT;
		if ("OUT".equals(direction))
			return DbParameterAccessor.OUTPUT;
		if ("IN/OUT".equals(direction))
			return DbParameterAccessor.INPUT_OUTPUT;
		// todo return val
		throw new UnsupportedOperationException("Direction " + direction + " is not supported");
	}

	public String buildInsertCommand(String tableName, DbParameterAccessor[] accessors) {

		StringBuilder sb = new StringBuilder("begin insert into ");
		sb.append(tableName).append("(");
		String comma = "";
		String retComma = "";

		StringBuilder values = new StringBuilder();
		StringBuilder retNames = new StringBuilder();
		StringBuilder retValues = new StringBuilder();

		for (DbParameterAccessor accessor : accessors) {
			int direction = accessor.getDirection();
			if (direction == DbParameterAccessor.INPUT || direction == DbParameterAccessor.SEQUENCE) {
				sb.append(comma);
				values.append(comma);
				sb.append(accessor.getName());
				// values.append(":").append(accessor.getName());
				values.append(accessor.getPlaceholder());
				comma = ",";
			}
			if (direction == DbParameterAccessor.OUTPUT || direction == DbParameterAccessor.SEQUENCE
					|| direction == DbParameterAccessor.INPUT_OUTPUT) {
				retNames.append(retComma);
				retValues.append(retComma);
				retNames.append(accessor.getName());
				retValues.append("?");
				retComma = ",";
			}
		}
		sb.append(") values (");
		sb.append(values);
		sb.append(")");
		if (retValues.length() > 0) {
			sb.append(" returning ").append(retNames).append(" into ").append(retValues);
		}
		sb.append("; end;");
		return sb.toString();
	}

	public String getFieldQuato() {
		return "\"";
	}

	/**
	 * {@inheritDoc} <br>
	 * <br>
	 * 在oracle中将java.util.Date转为java.sql.Date对象
	 */
	public Object converToSqlValue(Object value) {
		if (value != null && "java.util.Date".equals(value.getClass().getName())) {
			return new java.sql.Date(((java.util.Date) value).getTime());
		} else {
			return super.converToSqlValue(value);
		}
	}
}
