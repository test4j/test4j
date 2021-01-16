# Test4J

-------
test4j是一个单元测试和集成测试的框架，目前已经分拆成4个子项目

- [fluent-mock: mock框架](https://gitee.com/fluent-mybatis/fluent-mock.git)
- [fluent-assert: 断言框架](https://gitee.com/fluent-mybatis/fluent-assert.git)
- [fluent-dbtest: 数据库测试框架](https://gitee.com/fluent-mybatis/fluent-dbtest.git)
- [fluent-story: BDD测试框架](https://gitee.com/fluent-mybatis/fluent-story.git)

test4j项目作为一个总控集成框架继续存在, test4j提供了3个测试框架的总控集成
- test4j-junit4 使用junit4进行测试
- test4j-junit5 使用junit5进行测试
- test4j-testng 使用testng进行测试

## 使用
- maven pom.xml

```xml
<project>
<properties>
    <fluent-mock.version>1.0.0</fluent-mock.version>
    <test4j.version>2.7.1</test4j.version>
</properties>
<dependencies>
    <dependency>
        <groupId>org.test4j</groupId>
        <artifactId>test4j-junit4</artifactId>
        <!-- 
        <artifactId>test4j-junit5</artifactId>
        <artifactId>test4j-testng</artifactId>
        -->
        <version>${test4j.version}</version>
        <scope>test</scope>
    </dependency>
</dependencies>
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>2.22.2</version>
            <configuration>
                <useSystemClassLoader>true</useSystemClassLoader>
                <testFailureIgnore>true</testFailureIgnore>
                <parallel>all</parallel>
                <!-- 重要，argLine用于surefire插件启动fluent-mock -->
                <argLine>-javaagent:"${settings.localRepository}/org/test4j/fluent-mock/${fluent-mock.version}/fluent-mock-${fluent-mock.version}.jar</argLine>
                <forkMode>always</forkMode>
                <threadCount>1</threadCount>
            </configuration>
        </plugin>
    </plugins>
</build>
</project>
```

- gradle
```groovy
dependencies {
    testCompile('org.test4j:test4j-junit5:2.7.1')
    //annotationProcessor('org.test4j:fluent-mock:${fluent-mock.version}')
    testAnnotationProcessor('org.test4j:fluent-mock:1.0.0')

    test {
        jvmArgs "-javaagent:${classpath.find { it.name.contains("fluent-mock") }.absolutePath}"
        useJUnitPlatform()
    }
}
```