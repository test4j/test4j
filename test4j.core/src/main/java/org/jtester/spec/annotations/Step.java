package org.jtester.spec.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 场景测试中的步骤（可以当做 @Given @When @Then）
 * 
 * @author darui.wudr 2013-1-11 下午4:43:12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Step {

}
