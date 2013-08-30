Story 用例模板方法复用测试
GivenTemplate init condiction
初始化参数，
【列表=[1,2,3,4]】
【Map对象={"key1":"value1",
           "key2":"value2"}】


Scenario 模板方法验证，修改第一个参数
Given init condiction
【列表=[2,3,5]】

Then check condiction
验证参数，原始条件的值分别是
【列表=[2,3,5]】
【Map对象={"key1":"value1",
           "key2":"value2"}】
           
Scenario 模板方法验证，修改第二个参数
Given init condiction
【Map对象={"key11":"value1",
           "key12":"value2"}】

Then check condiction
验证参数，原始条件的值分别是,参数顺序和第一个场景对调
【Map对象={"key11":"value1",
           "key12":"value2"}】
【列表=[1,2,3,4]】