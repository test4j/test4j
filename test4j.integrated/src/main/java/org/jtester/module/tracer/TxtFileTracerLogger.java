package org.jtester.module.tracer;

import java.io.IOException;
import java.io.Writer;

import org.jtester.tools.commons.StringHelper;

@SuppressWarnings("rawtypes")
public class TxtFileTracerLogger extends TracerLogger {
	private StringBuilder buff = new StringBuilder();

	@Override
	public void writerMethodInputInfo(Class claz, String method, Object[] values) {
		buff.append("#START_INPUT#" + claz.getName() + "#" + method + "#" + values.length);
		buff.append("\n");
		for (int index = 1; index <= values.length; index++) {
			buff.append("#START_PARAMETER#" + index);
			buff.append("\n");
			buff.append(toJSON(values[index - 1]));
			buff.append("\n");
			buff.append("#END_PARAMETER");
			buff.append("\n");
		}
		buff.append("#END_INPUT");
		buff.append("\n");
	}

	@Override
	public void writerMethodException(Class claz, String method, Throwable exception) {
		buff.append("#START_EXCEPTION#" + claz.getName() + "#" + method);
		buff.append("\n");
		buff.append(exception.getMessage());
		buff.append("\n");
		buff.append("#END_EXCEPTION");
		buff.append("\n");
	}

	@Override
	public void writerMethodReturnValue(Class claz, String method, Object result) {
		buff.append("#START_RETURN#" + claz.getName() + "#" + method);
		buff.append("\n");
		buff.append(toJSON(result));
		buff.append("\n");
		buff.append("#END_RETURN");
		buff.append("\n");
	}

	@Override
	public void writerSqlStatement(String sql, Object result) {
		buff.append("#START_SQL");
		buff.append("\n");
		buff.append(sql);
		buff.append("\n");
		buff.append("#END_SQL");
		buff.append("\n");
	}

	@Override
	public void close() {
		String log = buff.toString();
		if (StringHelper.isBlankOrNull(log)) {
			return;
		}
		try {
			Writer writer = getWriter("txt");
			writer.write(log);
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
