package org.test4j.module.tracer;

import org.test4j.tools.commons.StringHelper;

public class DbTracerLogger extends XmlFileTracerLogger {
	@Override
	public void close() {
		String log = this.getTracerContext();
		if (StringHelper.isBlankOrNull(log)) {
			return;
		}
		try {
			log = "<test>\n" + log + "</test>";
			// TODO 记录数据库
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
