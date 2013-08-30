package org.jtester.module.dbfit.environment;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jtester.module.database.environment.DBEnvironment;
import org.jtester.module.database.environment.normalise.NameNormaliser;
import org.jtester.module.database.utility.DBHelper;
import org.jtester.module.dbfit.model.DbParameterAccessor;

@SuppressWarnings({ "rawtypes" })
public class DbFitSqlServerEnvironment extends DbFitAbstractDBEnvironment {

	public DbFitSqlServerEnvironment(DBEnvironment dbEnvironment) {
		super(dbEnvironment);
	}

	public boolean supportsOuputOnInsert() {
		return false;
	}

	private static String paramNamePattern = "@([A-Za-z0-9_]+)";
	private static Pattern paramRegex = Pattern.compile(paramNamePattern);

	public Pattern getParameterPattern() {
		return paramRegex;
	}

	protected String parseCommandText(String commandText, String[] vars) {
		if (vars == null || vars.length == 0) {
			return commandText;
		}
		String sql = commandText;
		for (String var : vars) {
			sql = sql.replace("@" + var, "?");
		}
		return sql;
	}

	public Map<String, DbParameterAccessor> getAllColumns(String tableOrViewName) throws SQLException {
		String qry = " select c.[name], TYPE_NAME(c.system_type_id) as [Type], c.max_length, "
				+ " 0 As is_output, 0 As is_cursor_ref " + " from sys.columns c "
				+ " where c.object_id = OBJECT_ID(?) " + " order by column_id";
		return readIntoParams(tableOrViewName, qry);
	}

	private Map<String, DbParameterAccessor> readIntoParams(String objname, String query) throws SQLException {
		if (objname.contains(".")) {
			String[] schemaAndName = objname.split("[\\.]", 2);
			objname = "[" + schemaAndName[0] + "].[" + schemaAndName[1] + "]";
		} else {
			objname = "[" + NameNormaliser.normaliseName(objname) + "]";
		}
		PreparedStatement dc = null;
		ResultSet rs = null;
		try {
			dc = this.connect().prepareStatement(query);
			dc.setString(1, NameNormaliser.normaliseName(objname));
			rs = dc.executeQuery();
			Map<String, DbParameterAccessor> allParams = new HashMap<String, DbParameterAccessor>();
			int position = 0;
			while (rs.next()) {
				String paramName = rs.getString(1);
				if (paramName == null)
					paramName = "";
				String dataType = rs.getString(2);
				// int length = rs.getInt(3);
				int direction = rs.getInt(4);
				int paramDirection;
				if (paramName.trim().length() == 0)
					paramDirection = DbParameterAccessor.RETURN_VALUE;
				else
					paramDirection = getParameterDirection(direction);
				DbParameterAccessor dbp = new DbParameterAccessor(paramName, paramDirection, getSqlType(dataType),
						getJavaClass(dataType), position++);
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

	/**
	 * List interface has sequential search, so using list instead of array to
	 * map types
	 */
	private static List<String> stringTypes = Arrays.asList(new String[] { "VARCHAR", "NVARCHAR", "CHAR", "NCHAR",
			"TEXT", "NTEXT", "UNIQUEIDENTIFIER" });
	private static List<String> intTypes = Arrays.asList(new String[] { "INT" });
	private static List<String> booleanTypes = Arrays.asList(new String[] { "BIT" });
	private static List<String> floatTypes = Arrays.asList(new String[] { "REAL" });
	private static List<String> doubleTypes = Arrays.asList(new String[] { "FLOAT" });
	private static List<String> longTypes = Arrays.asList(new String[] { "BIGINT" });
	private static List<String> shortTypes = Arrays.asList(new String[] { "TINYINT", "SMALLINT" });

	private static List<String> decimalTypes = Arrays
			.asList(new String[] { "DECIMAL", "NUMERIC", "MONEY", "SMALLMONEY" });
	private static List<String> timestampTypes = Arrays
			.asList(new String[] { "SMALLDATETIME", "DATETIME", "TIMESTAMP" });

	private static int getParameterDirection(int isOutput) {
		if (isOutput == 1)
			return DbParameterAccessor.OUTPUT;
		else
			return DbParameterAccessor.INPUT;
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
		// todo:strip everything from first blank
		dataType = normaliseTypeName(dataType);

		if (stringTypes.contains(dataType))
			return java.sql.Types.VARCHAR;
		if (decimalTypes.contains(dataType))
			return java.sql.Types.NUMERIC;
		if (intTypes.contains(dataType))
			return java.sql.Types.INTEGER;
		if (timestampTypes.contains(dataType))
			return java.sql.Types.TIMESTAMP;
		if (booleanTypes.contains(dataType))
			return java.sql.Types.BOOLEAN;
		if (floatTypes.contains(dataType))
			return java.sql.Types.FLOAT;
		if (doubleTypes.contains(dataType))
			return java.sql.Types.DOUBLE;

		if (longTypes.contains(dataType))
			return java.sql.Types.BIGINT;
		if (shortTypes.contains(dataType))
			return java.sql.Types.SMALLINT;

		throw new UnsupportedOperationException("Type " + dataType + " is not supported");
	}

	public Class getJavaClass(String dataType) {
		dataType = normaliseTypeName(dataType);
		if (stringTypes.contains(dataType))
			return String.class;
		if (decimalTypes.contains(dataType))
			return BigDecimal.class;
		if (intTypes.contains(dataType))
			return Integer.class;
		if (timestampTypes.contains(dataType))
			return java.sql.Timestamp.class;
		if (booleanTypes.contains(dataType))
			return Boolean.class;
		if (floatTypes.contains(dataType))
			return Float.class;
		if (doubleTypes.contains(dataType))
			return Double.class;
		if (longTypes.contains(dataType))
			return Long.class;
		if (shortTypes.contains(dataType))
			return Short.class;

		throw new UnsupportedOperationException("Type " + dataType + " is not supported");
	}

	public Map<String, DbParameterAccessor> getAllProcedureParameters(String procName) throws SQLException {
		return readIntoParams(procName, "select p.[name], TYPE_NAME(p.system_type_id) as [Type],  "
				+ " p.max_length, p.is_output, p.is_cursor_ref from sys.parameters p "
				+ " where p.object_id = OBJECT_ID(?) order by parameter_id ");

	}

	public String getFieldQuato() {
		return "";
	}
}
