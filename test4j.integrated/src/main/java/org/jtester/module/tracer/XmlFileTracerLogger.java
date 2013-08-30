package org.jtester.module.tracer;

import java.io.IOException;
import java.io.Writer;

import org.jtester.tools.commons.StringHelper;

@SuppressWarnings("rawtypes")
public class XmlFileTracerLogger extends TracerLogger {

	private StringBuilder buff = new StringBuilder();

	private int tabCount = 1;

	public void writerMethodInputInfo(Class claz, String method, Object[] values) {
		this.writeTab(1);
		buff.append(String.format("<call class=\"%s\" method=\"%s\">\n", claz.getName(), method));
		this.writeTab(1);
		buff.append(String.format("<paras count=\"%d\">\n", values.length));
		for (int index = 1; index <= values.length; index++) {
			this.writeTab(0);
			buff.append("<para>");
			buff.append("<![CDATA[");
			buff.append(toJSON(values[index - 1]));
			buff.append("]]>");
			buff.append("</para>\n");
		}
		this.tabCount--;
		this.writeTab(0);
		buff.append("</paras>\n");
	}

	public void writerMethodException(Class claz, String method, Throwable exception) {
		this.writeTab(-1);
		buff.append("<throwable><![CDATA[");
		if (exception == null) {
			buff.append("<null>");
		} else {
			buff.append(exception.getClass().getName()).append(":").append(exception.getMessage());
		}
		buff.append("]]></throwable>\n");
		// end call
		this.writeTab(0);
		buff.append("</call>\n");
	}

	public void writerMethodReturnValue(Class claz, String method, Object result) {
		this.writeTab(-1);
		buff.append("<return><![CDATA[");
		buff.append(toJSON(result));
		buff.append("]]></return>\n");
		// end call
		this.writeTab(0);
		buff.append("</call>\n");
	}

	public void writerSqlStatement(String sql, Object result) {
		this.writeTab(0);
		buff.append("<sql><![CDATA[");
		buff.append(sql);
		buff.append("]]></sql>\n");
	}

	protected String getTracerContext() {
		return buff.toString();
	}

	public void close() {
		String log = this.getTracerContext();
		if (StringHelper.isBlankOrNull(log)) {
			return;
		}
		try {
			Writer writer = getWriter("xml");
			writer.write("<test>\n");
			writer.write(log);
			writer.write("</test>");
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void writeTab(int step) {
		for (int index = 0; index < tabCount; index++) {
			buff.append("    ");
		}
		this.tabCount = tabCount + step;
	}
}
