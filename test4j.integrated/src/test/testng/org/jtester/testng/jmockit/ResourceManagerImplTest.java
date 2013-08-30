package org.jtester.testng.jmockit;

import java.util.Arrays;
import java.util.Collection;

import mockit.Mock;
import mockit.Mocked;
import mockit.Mockit;

import org.jtester.module.core.utility.MessageHelper;
import org.jtester.module.jmockit.demo1.ResourceManager;
import org.jtester.module.jmockit.demo1.ResourceManagerImpl;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SpringContext("org/jtester/fortest/spring/resourceManager.xml")
public class ResourceManagerImplTest extends JTester {

	@SpringBeanByName
	ResourceManager resourceManager;

	@Mocked
	ResourceManagerImpl mockResourceManager;

	@Test
	public void mockInitTest() {

		new Expectations() {
			{
				resourceManager.getResList("res1");
				times = 1;

				returns(Arrays.asList("", "", ""));

				resourceManager.getResList("res2");
				times = 1;
				minTimes = 0;
				maxTimes = 4;

				returns(Arrays.asList("", "", ""));
			}
		};

		Collection<?> coll = resourceManager.getResList("res1");
		want.collection(coll).notNull().sizeEq(3);

		Mockit.setUpMock(ResourceManagerImpl.class, MockResourceManager.class);
		resourceManager.init();
		want.bool(beenInited).isEqualTo(true);
		Collection<?> coll2 = resourceManager.getResList("res1");
		want.collection(coll2).notNull().sizeEq(2);
	}

	public static boolean beenInited = false;

	public static class MockResourceManager {
		@Mock
		public void init() {
			MessageHelper.info("mock resource manager init");
			beenInited = true;
		}

		@Mock
		public Collection<?> getResList(String resName) {
			return Arrays.asList("", "");
		}
	}
}
