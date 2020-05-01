//package cn.org.atool.fluent.mybatis.generator;
//
//import org.test4j.generator.mybatis.config.DataSourceConfig;
//import org.test4j.generator.mybatis.config.ITypeConvert;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.experimental.Accessors;
//import org.test4j.generator.mybatis.model.TableInfo;
//import org.test4j.generator.mybatis.rule.DbType;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Consumer;
//
//import static java.util.stream.Collectors.joining;
//
///**
// * @author darui.wu
// */
//@Accessors(chain = true)
//public class TableConvert {
//
//
//    @Setter
//    @Getter
//    private String entitySuffix = "Entity";
//
//
//    @Setter
//    private MybatisGenerator mybatisGenerator;
//
//    @Setter
//    @Getter
//    private List<Class> modelInterface = new ArrayList<>();
//
////    public TableConvert() {
////        this.prefix = new String[0];
////    }
////
////    public TableConvert(String... prefix) {
////        this.prefix = prefix;
////    }
////
////    public TableInfo table(String tableName, String entityName) {
////        TableInfo table = new TableInfo(this, tableName, precessEntityName(entityName));
////        this.tables.put(tableName, table);
////        return table;
////    }
//
//    private String precessEntityName(String entityName) {
//        if (entityName.endsWith(entitySuffix)) {
//            return entityName.substring(0, entityName.length() - entitySuffix.length());
//        } else {
//            return entityName;
//        }
//    }
//
//
//    public TableConvert addModelInterface(Class klass) {
//        this.modelInterface.add(klass);
//        return this;
//    }
//
//    public String getInterfacePacks() {
//        return modelInterface.stream()
//            .map(klass -> "import " + klass.getName() + ";")
//            .collect(joining("\n"));
//    }
//
//    public String getInterfaceNames() {
//        return modelInterface.stream()
//            .map(Class::getSimpleName)
//            .collect(joining(", "));
//    }
//}
//
