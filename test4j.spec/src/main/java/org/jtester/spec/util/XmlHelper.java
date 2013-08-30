package org.jtester.spec.util;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.jtester.spec.scenario.xmlparser.StoryQName;
import org.jtester.tools.commons.StringHelper;

/**
 * xml解析辅助工具类
 * 
 * @author darui.wudr 2012-7-12 上午9:49:35
 */
public class XmlHelper {
	/**
	 * 从classpath xml文件构建dom4j的文档对象
	 * 
	 * @param file
	 * @return
	 */
	public static Document buildFromClasspath(String file, String encoding) {
		try {
			InputStream is = XmlHelper.class.getClassLoader().getResourceAsStream(file);
			if (is == null) {
				throw new RuntimeException("can't find classpath file[" + file + "].");
			}
			return buildFromStream(is, encoding);
		} catch (Exception e) {
			throw new RuntimeException("build document from classpath file[" + file + "] error.", e);
		}
	}

	/**
	 * 从输入流中构建dom4j文档对象
	 * 
	 * @param is
	 * @param encoding
	 * @return
	 */
	public static Document buildFromStream(InputStream is, String encoding) {
		try {
			SAXReader reader = new SAXReader();
			if (StringHelper.isEmpty(encoding) == false) {
				reader.setEncoding(encoding);
			}
			Document document = reader.read(is);
			return document;
		} catch (Exception e) {
			throw new RuntimeException("build document from InputStream error:" + e.getMessage(), e);
		}
	}

	/**
	 * 从文本对象构建dom4j文档对象
	 * 
	 * @param xml
	 * @return
	 */
	public static Document buildFromString(String xml) {
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(new StringReader(xml));
			return document;
		} catch (Exception e) {
			throw new RuntimeException("build document from string error.", e);
		}
	}

	private static String textFormat = "<root>%s</root>";

	/**
	 * 从xml片段代码中解析出节点列表
	 * 
	 * @param xml
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<Node> parseNodesFromText(String xml) {
		Document doc = XmlHelper.buildFromString(String.format(textFormat, xml));
		List<Node> nodes = new ArrayList<Node>();
		for (Iterator it = doc.getRootElement().nodeIterator(); it.hasNext();) {
			Node item = (Node) it.next();
			if (item instanceof Element) {
				String name = ((Element) item).attributeValue(StoryQName.attrName);
				if (StringHelper.isBlankOrNull(name)) {
					throw new RuntimeException("the name of para can't be null!");
				}
			}
			nodes.add(item);
		}
		return nodes;
	}

	private final static String METHOD_PARAMETER_REG = "(.*(<para\\s+name=\"[^<>]*\"\\s*>.*</para>).*)";

	public static List<Node> parseNodesFromMethodText(String xml) {
		List<Node> nodes = new ArrayList<Node>();
		if (StringHelper.isBlankOrNull(xml)) {
			return nodes;
		}

		new StringTokenizer(xml, METHOD_PARAMETER_REG);
		String[] items = xml.split(METHOD_PARAMETER_REG);
		for (String item : items) {
			System.out.println(item);
		}
		return nodes;
	}

	/**
	 * 将xml文档对象转换为xml文本
	 * 
	 * @param document
	 * @return
	 */
	public static String toXml(Document document) {
		try {
			Writer writer = new StringWriter();
			document.write(writer);
			String xml = writer.toString();
			return xml;
		} catch (Exception e) {
			throw new RuntimeException("convert doucment object to xml string error.", e);
		}
	}

	public static List<MethodNode> parseMethodContext(String context) {
		MethodNodeParser parser = new MethodNodeParser(context);
		List<MethodNode> nodes = parser.parseMethodNodes();
		return nodes;
	}

	public static class MethodNode {
		final boolean isPara;
		String paraName;
		String text;

		/**
		 * 文本节点
		 */
		public MethodNode() {
			this.isPara = false;
		}

		public MethodNode(boolean isPara) {
			this.isPara = isPara;
		}

		/**
		 * 文本节点
		 */
		public MethodNode(String text) {
			this.isPara = false;
			this.text = text;
		}

		/**
		 * 参数节点
		 */
		public MethodNode(String name, String text) {
			this.isPara = true;
			this.paraName = name;
			this.text = text;
		}

		public String getParaName() {
			return paraName;
		}

		public MethodNode setParaName(String paraName) {
			this.paraName = paraName;
			return this;
		}

		public String getText() {
			return text;
		}

		public MethodNode setText(String text) {
			this.text = text;
			return this;
		}

		public boolean isPara() {
			return isPara;
		}
	}
}
