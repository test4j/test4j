package org.test4j.junit5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.test4j.example.mix.ServiceMix;
import org.test4j.example.spring.SpringConfig;
import org.test4j.module.spec.IStory;
import org.test4j.module.spec.annotations.Mix;

@ContextConfiguration(classes = SpringConfig.class)
public class StoryTestDemo extends Test4J implements IStory {
    @Mix
    private ServiceMix serviceMix;

    @DisplayName("story测试场景演示")
    @Test
    public void test_one_display_name() {
        story.scenario("story测试场景演示")
                .given("前置条件", () -> serviceMix.do_given_method("my condition"))
                .when("执行测试", () -> System.out.println("执行测试......"))
                .then("后置验证", () -> serviceMix.do_then_method("hello, spec"))
        ;
    }

    @DisplayName("story测试场景演示")
    @Test
    public void test_two_display_name() {
        story.scenario()
                .given("前置条件", () -> serviceMix.do_given_method("my condition"))
                .when("执行测试", () -> System.out.println("执行测试......"))
                .then("后置验证", () -> serviceMix.do_then_method("hello, spec"))
        ;
    }
}
