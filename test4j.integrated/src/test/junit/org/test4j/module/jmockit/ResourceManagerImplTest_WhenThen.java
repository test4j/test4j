package org.test4j.module.jmockit;

import java.util.Arrays;
import java.util.Collection;

import mockit.Mocked;

import org.junit.Test;
import org.test4j.junit.Test4J;
import org.test4j.module.jmockit.demo1.ResourceManager;
import org.test4j.module.jmockit.demo1.ResourceManagerImpl;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;

@SpringContext("org/test4j/fortest/spring/resourceManager.xml")
public class ResourceManagerImplTest_WhenThen implements Test4J {
    @SpringBeanByName
    ResourceManager     resourceManager;

    @Mocked
    ResourceManagerImpl mockResourceManager;

    @Test
    public void mockInitTest() {
        new Expectations() {
            {
                when(resourceManager.getResList("res1")).callBetween(1, 2).thenReturn(Arrays.asList("", "", ""),
                        Arrays.asList(""));
            }
        };

        Collection<?> coll = resourceManager.getResList("res1");
        want.collection(coll).notNull().sizeEq(3);

        Collection<?> coll2 = resourceManager.getResList("res1");
        want.collection(coll2).notNull().sizeEq(1);
    }

    @Test(expected = RuntimeException.class)
    public void mockInitTest_exception() {
        new Expectations() {
            {
                when(resourceManager.getResList("res1")).callExactly(1).thenThrows(new RuntimeException());
            }
        };
        resourceManager.getResList("res1");
    }
}
