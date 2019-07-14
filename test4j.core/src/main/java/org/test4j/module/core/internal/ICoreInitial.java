package org.test4j.module.core.internal;

import org.test4j.module.database.dbop.DBOperator;
import org.test4j.module.database.dbop.IDBOperator;
import org.test4j.module.spring.interal.ISpringHelper;

/**
 * ICore接口类静态变量初始化工具类
 *
 * @author darui.wudr 2013-1-15 上午10:07:53
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ICoreInitial {

    public static ISpringHelper initSpringHelper() {
        return new ISpringHelper() {
        };
    }

    public static IDBOperator initDBOperator() {
        return new DBOperator();
    }
}
