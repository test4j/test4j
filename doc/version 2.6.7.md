- 代码生成时，增加指定字段属性
    1. 是否大字段
    2. update默认值
    3. insert默认值
``` java
ITableSetter setColumn(String column, Consumer<DefinedColumn> consumer);
```