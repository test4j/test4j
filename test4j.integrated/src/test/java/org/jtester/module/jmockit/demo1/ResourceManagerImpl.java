package org.jtester.module.jmockit.demo1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jtester.module.core.utility.MessageHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

@SuppressWarnings({ "rawtypes" })
public class ResourceManagerImpl implements ResourceManager, BeanFactoryAware {

	private static Map<String, Map> resources;

	public String getOptionValue(String resName, String key) {
		Map map = resources.get(resName);
		if (map == null) {
			return null;
		}
		return (String) map.get(key);
	}

	public Object getResItem(String resName, String key) {
		Map map = resources.get(resName);
		if (map == null) {
			return null;
		}
		return map.get(key);
	}

	public Object[] getResItems(String resName, String[] keys) {
		Map map = resources.get(resName);
		if (map == null) {
			return null;
		}
		List<Object> objs = new ArrayList<Object>();
		for (String key : keys) {
			if (map.containsKey(key)) {
				objs.add(map.get(key));
			}
		}
		return objs.toArray(new Object[0]);
	}

	public Collection<?> getResList(String resName) {
		Map map = resources.get(resName);
		return map == null ? null : map.values();
	}

	public void init() {
		MessageHelper.info("init resource Manager");
		// assert false == true : "can't be execute";
		resources = new HashMap<String, Map>();
		resources.put("res1", new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				this.put("key11", "value11");
				this.put("key12", "value12");
			}
		});
		resources.put("res2", new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				this.put("key21", "value21");
				this.put("key22", "value22");
			}
		});
	}

	BeanFactory beanFactory;

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
}
