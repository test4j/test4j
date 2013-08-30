package org.test4j.spec.spring;

import java.util.List;

import org.test4j.hamcrest.matcher.property.reflection.EqMode;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.spec.annotations.Given;
import org.test4j.spec.annotations.Named;
import org.test4j.spec.annotations.StoryFile;
import org.test4j.spec.annotations.StoryType;
import org.test4j.spec.annotations.Then;
import org.test4j.spec.annotations.When;
import org.test4j.spec.inner.IScenario;
import org.testng.annotations.Test;

@StoryFile(type = StoryType.TXT)
public class SpringSpecDemo2 extends SpringBaseDemo {

    @SpringBeanByName
    UserService userService;

    @Given
    public void readyData() {
        System.out.println("====");
    }

    @When
    public void queryUser(@Named("user") String name) {
        this.users = this.userService.getUserByName(name);
        System.out.println("xxxxxxx");
    }

    @Then
    public void checkData(@Named("userList") String users) {
        String[] arr = users.split(",");
        want.list(this.users).reflectionEq(arr, EqMode.IGNORE_ORDER);
        System.out.println("sssssss===");
        // throw new RuntimeException("xxxxxx");
    }

    List<String> users;

    @Test(dataProvider = "story")
    @Override
    public void runScenario(IScenario scenario) throws Throwable {
        this.run(scenario);
    }
}
