package cn.org.atool.fluent.mybatis.generator.mapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AddressMP
 * @Description address映射定义
 *
 * @author ${author}
 */
public interface AddressMP {
    /**
     * 实例属性和数据库字段对应表
     */
    Map<String, String> Property2Column = new HashMap<String,String>(){
        {
            this.put(Property.address, Column.address);
        }
    };

    /**
     * 表名称
     */
    String Table_Name = "address";
    /**
    * 实体名称
    */
    String Entity_NAME = "${entityName}";

    /**
     * 表address字段定义
     */
    interface Column{
        /**
         * 
         */
        String address = "address";
    }

    /**
     * 对象${entityName}属性字段
     */
    interface Property{
        /**
         * 
         */
        String address = "address";
    }
}