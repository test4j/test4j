Story 验证跳过测试步骤功能

Scenario 本场景中包含3个执行步骤，无跳过步骤
Given input init string
传入【初始值=init text】

Given append this string
把【参数=append text】添加到初始值后面

Then check final text
因为步骤2被跳过，所以最后的【合并字符串=init text,append text】

Scenario 本场景中包含2个执行步骤，1个跳过步骤
Given input init string
传入【初始值=init text】

SkipGiven append this string
把【参数=append text】添加到初始值后面

Then check final text
因为步骤2被跳过，所以最后的【合并字符串=init text】