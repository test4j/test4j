# Test4J
[TOC]

-------
test4j是一个单元测试和集成测试的框架，它提供了以下特性
- 和junit和testng的集成使用
- 支持Spring和SpringBoot测试
- 提供了fluent api形式的断言
- 以java形式进行数据库准备和数据库断言
- 对数据库执行sql语句进行断言
- 场景化业务测试，模块化步骤
- 和jmockit组合使用

##使用##
- maven pom.xml文件定义

```xml
<properties>
    <jmockit.version>1.4.5</jmockit.version>
</properties>
<dependencies>
    <dependency>
        <groupId>org.test4j</groupId>
        <artifactId>test4j</artifactId>
        <version>2.5.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.jmockit</groupId>
        <artifactId>jmockit</artifactId>
        <version>${jmockit.version}</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
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
                <!-- 重要，argLine用于surefire插件启动jmockit和test4j -->
                <argLine>-javaagent:"${settings.localRepository}/org/jmockit/jmockit/${jmockit.version}/jmockit-${jmockit.version}.jar -Dfakes=org.test4j.junit4.Test4JBuilder</argLine>
                <forkMode>always</forkMode>
                <threadCount>1</threadCount>
            </configuration>
        </plugin>
    </plugins>
</build>
```
