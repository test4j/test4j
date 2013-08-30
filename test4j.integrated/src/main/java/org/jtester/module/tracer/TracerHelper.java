package org.jtester.module.tracer;

import org.jtester.json.encoder.object.SpecEncoder;
import org.jtester.module.core.utility.IPropConst;
import org.jtester.tools.commons.ClazzHelper;
import org.jtester.tools.commons.ConfigHelper;
import org.jtester.tools.commons.StringHelper;

@SuppressWarnings("rawtypes")
public class TracerHelper {

	/**
	 * 记录跟踪信息的媒介，type文件，db数据库
	 */
	public final static String TRACER_TYPE = "tracer.type";
	/**
	 * 如果信息媒介是文件，文件存放的根路径<br>
	 * 如果没有设置，则表示在项目target\tracer路径下
	 */
	public final static String TRACER_FILE_DIR = "tracer.file.dir";
	/**
	 * 如果是记录在数据库中，数据库连接串url
	 */
	public final static String TRACER_DB_URL = "tracer.db.url";
	/**
	 * 如果是记录在数据库中，数据库连接用户名
	 */
	public final static String TRACER_DB_USERNAME = "tracer.db.username";
	/**
	 * 如果是记录在数据库中，数据库连接密码
	 */
	public final static String TRACER_DB_PASSWORD = "tracer.db.password";

	/**
	 * 是否打开记录测试序列的功能
	 * 
	 * @return
	 */
	public static boolean doesTracerEnabled() {
		return IPropConst.TRACER_ENABLE;
	}

	/**
	 * 是否将跟踪信息记录在文件中
	 * 
	 * @return
	 */
	public static boolean tracerLogInFile() {
		String type = ConfigHelper.getString(TRACER_TYPE, "file");
		if ("file".equalsIgnoreCase(type)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 存放跟踪信息的文件根路径
	 * 
	 * @return
	 */
	public static String tracerFileDir() {
		String dir = ConfigHelper.getString(TRACER_FILE_DIR);
		if (StringHelper.isBlankOrNull(dir)) {
			dir = System.getProperty("user.dir") + "/target/tracer";
		}
		return dir;
	}

	/**
	 * 排除一些复杂对象JSON生成
	 */
	public static void addCustomizedSpecEncoder() {
		try {
			Class sqlMapClientImpl = ClazzHelper.getClazz("com.ibatis.sqlmap.engine.impl.SqlMapClientImpl");
			SpecEncoder.addSpecEncoder(sqlMapClientImpl, IbatisSqlMapClientImplEncoder.instance);
		} catch (Throwable e) {

		}
	}
}
