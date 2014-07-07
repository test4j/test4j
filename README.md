
##什么是Test4J##
Test4J原名叫jTester,本来是发布在google上的一个[开源项目](http://code.google.com/jtester)，后来迁移到github，并且由于域名的缘故，更名为[Test4J](https://github.com/test4j。Test4J是一个单元测试和业务流程测试框架，其基本功能包括如下：
1. 基本的单元测试功能
	- Fluent方式的断言，内置了大部分常用的断言语法，特别是对象反射断言功能尤其强大。
	- Junit和testNg语法扩展，使用@DataFrom方式扩展junit的数据驱动测试功能；@Group语法让junit支持分组测试；模块嵌入的方式让junit和testng支持功能扩展。
	- 集成jMockit框架，让mock更自由自在。
	- 对象自动填充功能，反射工具。
2. 集成测试工具包
	- 支持Spring集成测试，spring容器可以mock对象，自定义对象无缝集成。
	- 数据库测试支持，使用DataMap对象，Json数据准备数据，或者验证数据，同时支持数据库数据的Fluent断言。
3. 业务驱动测试工具包
	- 支持编写可读的用例，并在用例中嵌入测试用数据，框架自动转换为可执行代码。
	- 支持用例步骤的重复利用，简化用例编写难度。

### maven引用 ###
以上3个功能功能模块，对应具体的jar包。

| 功能模块      |    jar包 |   
| :-------- | :--------| :--: |  
| 单元测试  | test4j.nodep, test4j.core, test4j.junit或test4j.testng<br>如果使用junit就引用test4j.junit, 使用testng就引用test4j.testng<br>test4j.junit和test4j.testng会间接引入core包和nodep包|
| 集成测试     |  所有单元测试包 + test4j.integrated | 
| 业务驱动测试   |  所有集成测试包  + test4j.spec |
单元测试jar引用
```xml
	<dependency>
		<groupId>org.test4j</groupId>
		<artifactId>test4j.junit</artifactId>
		<version>2.0.6</version>
	</dependency>   
``` 
或
```xml
	<dependency>
		<groupId>org.test4j</groupId>
		<artifactId>test4j.testng</artifactId>
		<version>2.0.6</version>
	</dependency>   
``` 
集成测试jar引用
```xml
	<dependency>
		<groupId>org.test4j</groupId>
		<artifactId>test4j.junit</artifactId>
		<version>2.0.6</version>
	</dependency>   
	<dependency>
		<groupId>org.test4j</groupId>
		<artifactId>test4j.integrated</artifactId>
		<version>2.0.6</version>
	</dependency> 
``` 
或
```xml
	<dependency>
		<groupId>org.test4j</groupId>
		<artifactId>test4j.testng</artifactId>
		<version>2.0.6</version>
	</dependency>   
	<dependency>
		<groupId>org.test4j</groupId>
		<artifactId>test4j.integrated</artifactId>
		<version>2.0.6</version>
	</dependency> 
``` 
业务测试jar引用
```xml
	<dependency>
		<groupId>org.test4j</groupId>
		<artifactId>test4j.junit</artifactId>
		<version>2.0.6</version>
	</dependency>   
	<dependency>
		<groupId>org.test4j</groupId>
		<artifactId>test4j.spec</artifactId>
		<version>2.0.6</version>
	</dependency> 
``` 
或
```xml
	<dependency>
		<groupId>org.test4j</groupId>
		<artifactId>test4j.testng</artifactId>
		<version>2.0.6</version>
	</dependency>   
	<dependency>
		<groupId>org.test4j</groupId>
		<artifactId>test4j.spec</artifactId>
		<version>2.0.6</version>
	</dependency> 
``` 