package org.jtester.module.database.environment.typesmap;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "rawtypes" })
public interface DB2TypeMap {
	Map<String, Class> java = new HashMap<String, Class>() {
		private static final long serialVersionUID = 2634796890699526702L;

		{
		}
	};

	Map<String, Integer> sql = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 458255708680362751L;

		{
		}
	};
}
