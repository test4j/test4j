package org.jtester.testng.jmockit;

import java.util.Arrays;
import java.util.Collection;

import mockit.Mocked;

import org.jtester.module.jmockit.demo1.ResourceManager;
import org.jtester.module.jmockit.demo1.ResourceManagerImpl;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SpringContext("org/jtester/fortest/spring/resourceManager.xml")
@Test(groups = { "jtester", "jmockit" })
public class ResourceManagerImplTest_WhenThen extends JTester {
	@SpringBeanByName
	ResourceManager resourceManager;

	@Mocked
	ResourceManagerImpl mockResourceManager;

	@Test
	public void mockInitTest() {
		new Expectations() {
			{
				when(resourceManager.getResList("res1")).callBetween(1, 2)
						.thenReturn(Arrays.asList("", "", ""),
								Arrays.asList(""));
			}
		};

		Collection<?> coll = resourceManager.getResList("res1");
		want.collection(coll).notNull().sizeEq(3);

		Collection<?> coll2 = resourceManager.getResList("res1");
		want.collection(coll2).notNull().sizeEq(1);
	}

	@Test(expectedExceptions = RuntimeException.class)
	public void mockInitTest_exception() {
		new Expectations() {
			{
				when(resourceManager.getResList("res1")).callExactly(1)
						.thenThrows(new RuntimeException());
			}
		};
		resourceManager.getResList("res1");
	}
}
