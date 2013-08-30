Stroy JSpec各种类型参数转换验证

Scenario 各种基本类型转换
When convert to primitive type
【布尔值=true】
【整型=123】
【长整型=234】
【短整型=23】
【浮点数=23.12】
【双精度=234.23】
【字符串=I am a String】

Scenario 大数据类型转换
When convert to big decimal and big integer
【大整数=123456789123456789】
【大数字=123456789123456789.12345】

Scenario 日期转换验证
When convert date
【日期=2010-12-20】
【时间=12:30:45】
【日期加时间=2010-04-14 14:23:57】
【java_sql_Date=2010-04-14】
【java_sql_Timestamp=2010-04-14 14:23:57】

Scenario 一维数组转换
When convert to int array
【数组=[1,2,3]】

Scenario 二维数组转换
When convert to bool array
【数组=[[true, false],
		[true, true]]】

Scenario list 转换
When convert to List
【列表=[true,false]】

Scenario 验证list泛型转换
When convert to list generic
【列表=[{'first':'wu','last':'davey'},{'first':'he','last':'jobs'}]】
When convert to list hashmap
【列表=[{'first':'wu','last':'davey'},{'first':'he','last':'jobs'}]】

Scenario 验证Dto转换
When convet to dto
【用户={'first':'wu','last':'davey'}】

Scenario DataMap和HashMap转换验证
When convert to map
【DataMap={'first':'wu','last':'davey'}】
【HashMap={'first':'wu','last':'davey'}】