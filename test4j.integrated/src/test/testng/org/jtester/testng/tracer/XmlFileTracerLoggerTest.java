package org.jtester.testng.tracer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import mockit.Mock;

import org.jtester.module.tracer.TracerLogger;
import org.jtester.module.tracer.TracerServiceDemo;
import org.jtester.module.tracer.XmlFileTracerLogger;
import org.jtester.testng.JTester;
import org.jtester.tools.commons.ResourceHelper;
import org.testng.annotations.Test;

@Test(groups = "tracer")
public class XmlFileTracerLoggerTest extends JTester {

	@Test
	public void testXmlTracer() throws FileNotFoundException {
		final StringWriter writer = new StringWriter();
		new MockUp<TracerLogger>() {
			@Mock
			public Writer getWriter(String surfix) throws IOException {
				return writer;
			}
		};
		TracerLogger log = new XmlFileTracerLogger();

		log.writerMethodInputInfo(TracerServiceDemo.class, "sayHello", new Object[] { 1, 2, "name", true });
		log.writerMethodInputInfo(TracerServiceDemo.class, "sayHelloInternal", new Object[] { 1, 2, "name", true });
		log.writerSqlStatement("select * from tdd_user", "");
		log.writerSqlStatement("update tdd_user set first_name='xxxx' where id=124", "");
		log.writerMethodReturnValue(TracerServiceDemo.class, "sayHelloInternal", "your value");
		log.writerMethodException(TracerServiceDemo.class, "sayHello", new RuntimeException("call error"));

		log.close();
		String xml = writer.toString();
		String expected = ResourceHelper.readFromFile("org/jtester/module/tracer/XmlFileTracerLoggerTest.xml");
		want.string(xml).isEqualTo(expected);
	}
}
