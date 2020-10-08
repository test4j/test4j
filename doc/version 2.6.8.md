- 生成文件时, 调整了部分策略
    1. 增加生成文件时的重写开关
    2. 生成文件时dao package和其它类的package分开设置
    3. 生成Entity时, 去掉ColumnJavaType类, 直接使用java class来标识Entity属性类型