###2.0.7###
- TestScenario toString改进
- 初始化测试类异常打印
- Mix对象不再强制继承Steps接口

###2.0.6###
- 场景测试增加BeforeScenario， AfterScenario关键字。<br> 
	BeforeScenario中定义的所有方法在每个场景之前执行。<br>
	AfterScenario中定义的所有方法在每个场景之后执行。
- 针对SIndex vm设置，增加简化写法si，方便执行单条测试。
- 增加场景测试编号
- fix SpringBeanFrromFactory初始化的nullpointer异常。

###2.0.5###
- jmockit升级到1.5版本
- Steps类中如果声明了SpringBean，框架会自动进行注入处理
- 支持@Autowired和@Resource的bean注入方式
- remove declared @Test from org.test4j.testng.Test4J