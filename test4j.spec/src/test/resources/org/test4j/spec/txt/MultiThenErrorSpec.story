Story 验证框架运行then检查的非阻塞式异常

Scenario 有多个then错误
Given init normal
准备数据正常，无异常抛出
When execute normal
正常执行业务方法，无异常抛出
Then check error
检查数据发生错误【错误消息=第一个错误】
Then check normal
正常检查结果，无异常抛出
Then check error
检查数据发生错误【错误消息=第二个错误】
Then check scenario error
运行当前测试场景，应该抛出多个then异常

Scenario 正常运行，无任何错误抛出
Given init normal
准备数据正常，无异常抛出
When execute normal
正常执行业务方法，无异常抛出
Then check normal
正常检查结果，无异常抛出
Then check scenario normal
运行当前测试场景，应该无任何异常

Scenario 运行测试场景时，在given方法中有异常抛出
Given check scenario exception
运行本测试场景，应该会抛出一个普通的异常
Given init error
准备数据，抛出一个异常

