BeforeScenario
Given set var
	设置【变量值=23】
	
Scenario 给变量值加增量
When add var
	给变量值增加【增量=10】
	
Then check var
	检查【变量值=33】
	

	