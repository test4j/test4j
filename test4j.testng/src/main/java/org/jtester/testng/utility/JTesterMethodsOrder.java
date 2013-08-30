package org.jtester.testng.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

/**
 * 测试方法排序
 * 
 * @author darui.wudr
 * 
 */
public class JTesterMethodsOrder implements IMethodInterceptor {

	public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
		Map<String, List<IMethodInstance>> map = new HashMap<String, List<IMethodInstance>>();
		for (IMethodInstance mi : methods) {
			Object instance = mi.getInstance();
			String clazzname = instance.getClass().getName();

			List<IMethodInstance> list = map.get(clazzname);
			if (list == null) {
				list = new ArrayList<IMethodInstance>();
				map.put(clazzname, list);
			}
			list.add(mi);
		}

		List<IMethodInstance> order = new ArrayList<IMethodInstance>();
		for (String clazzname : map.keySet()) {
			List<IMethodInstance> list = map.get(clazzname);
			order.addAll(list);
		}
		System.out.println("\n\n\n");
		for (IMethodInstance mi : order) {
			System.out.println(mi.getInstance().getClass().getName());
		}
		System.out.println("\n\n\n");
		return order;
	}
}
