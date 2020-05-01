//package cn.org.atool.fluent.mybatis.generator;
//
//import org.test4j.generator.mybatis.rule.IColumnType;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.experimental.Accessors;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//@Getter
//@Accessors(chain = true)
//public class Table  {
//
//
//    @Setter
//    private String withoutSuffixEntity;
//

//
////    public String getWithSuffixEntity() {
////        return this.withoutSuffixEntity + convertor.getEntitySuffix();
////    }
//
//
//
////    public Optional<TableColumn> getColumn(String column) {
////        return Optional.ofNullable(this.columns.get(column));
////    }
//
////    public IColumnType columnType(String column) {
////        return this.getColumn(column).map(TableColumn::getColumnType).orElse(null);
////    }
////
////
////    public String getPropertyNameByColumn(String column) {
////        return this.getColumn(column).map(TableColumn::getPropertyName).orElse(null);
////    }
//
////    @Override
////    public int compareTo(Table table) {
////        return this.tableName.compareTo(table.getTableName());
////    }
//

//}
