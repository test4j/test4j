package org.test4j.testng.jmockit;

import java.util.Arrays;
import java.util.Collection;

import mockit.Mocked;

import org.test4j.module.jmockit.demo1.ResourceManager;
import org.test4j.module.jmockit.demo1.ResourceManagerImpl;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.testng.JTester;
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
