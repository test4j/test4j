package org.jtester.module.tracer;

import java.io.Writer;
import java.util.List;

import org.jtester.json.encoder.object.SpecEncoder;

import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;

public class IbatisSqlMapClientImplEncoder extends SpecEncoder<SqlMapClientImpl> {

	public static final IbatisSqlMapClientImplEncoder instance = new IbatisSqlMapClientImplEncoder();

	public IbatisSqlMapClientImplEncoder() {
		super(SqlMapExecutorDelegate.class);
	}

	@Override
	protected void encodeSpec(SqlMapClientImpl target, Writer writer, List<String> references) throws Exception {
		writer.append(String.valueOf(target));
	}
}
