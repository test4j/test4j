package org.jtester.json.encoder.object.spec;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.List;

import org.jtester.json.encoder.object.SpecEncoder;

@SuppressWarnings("rawtypes")
public class MethodEncoder extends SpecEncoder<Method> {
	public static MethodEncoder instance = new MethodEncoder();

	public static final String JSON_METHOD_NAME = "methodName";

	public static final String JSON_METHOD_DECLAREDBY = "declaredBy";

	public static final String JSON_METHOD_PARATYPE = "paraType";

	protected MethodEncoder() {
		super(Method.class);
	}

	@Override
	protected void encodeSpec(Method target, Writer writer, List<String> references) throws Exception {
		String name = target.getName();
		String claz = target.getDeclaringClass().getName();
		Class[] paraTypes = target.getParameterTypes();
		writer.append('{');
		this.writerSpecProperty(JSON_METHOD_NAME, writer);
		writer.write(":");
		writer.append(quote_Char).append(name).append(quote_Char);
		writer.append(',');
		this.writerSpecProperty(JSON_METHOD_DECLAREDBY, writer);
		writer.write(":");
		writer.append(quote_Char).append(claz).append(quote_Char);
		writer.append(',');
		this.writerSpecProperty(JSON_METHOD_PARATYPE, writer);
		writer.append(':');
		this.writerParaTypes(writer, paraTypes);
		writer.append('}');
	}

	private void writerParaTypes(Writer writer, Class[] types) throws IOException {
		writer.append('[');
		boolean isFirst = true;
		for (Class type : types) {
			if (isFirst) {
				isFirst = false;
			} else {
				writer.append(',');
			}
			writer.append(quote_Char).append(type.getName()).append(quote_Char);
		}
		writer.append(']');
	}
}
