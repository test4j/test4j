2.0.2
o 优化JSpec执行场景步骤抛出异常时的描述信息。
o 改进JSpec执行测试场景的printer实现
o 优化JSPec参数转换错误的提醒信息
o 增加JSpec @Mix功能


2.0.1
o 修复ClassFieldInfo中对set方法的判断。
o 增加在控制台输出javaagent提示信息
o spring模块增加是否允许强制懒加载的选项
o 使用db.table方式往数据库中插入数据时，如果是Enum对象，直接获取枚举值的name()
o 增加了@TestedObject功能
o 修正数据库配置错误时，输出log无法定位具体错误的问题
o 修改ICore, IDatabase, ISpring等实现方式。junit测试实现JTester接口，testng测试继承JTester基类。
