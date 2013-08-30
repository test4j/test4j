package org.jtester.module.spring.annotations;

import java.util.HashMap;

import org.jtester.fortest.reflector.MyService;

@SuppressWarnings("rawtypes")
public interface ExMyService extends MyService {
	public String protectedInvoked();

	public String privateInvoked(String in) throws Throwable;

	public int primitivePara(int i, boolean bl);

	public int mapPara(HashMap map);

	public void invokeException();
}
