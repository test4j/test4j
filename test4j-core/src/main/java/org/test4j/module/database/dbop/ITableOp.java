package org.test4j.module.database.dbop;

import org.test4j.asserts.iassert.intf.ICollectionAssert;
import org.test4j.asserts.iassert.intf.INumberAssert;
import org.test4j.asserts.iassert.intf.IObjectAssert;
import org.test4j.tools.datagen.IDataMap;

/**
 * ITableOp
 *
 * @author wudarui
 */
@SuppressWarnings("rawtypes")
public interface ITableOp {
    /**
     * 清空数据表
     *
     * @return
     */
    ITableOp clean();

    /**
     * 根据Map的key（表字段）插入数据
     *
     * @param datas
     * @return
     */
    ITableOp insert(IDataMap... datas);

    /**
     * 查询表数据，做数据断言
     *
     * @return
     */
    ICollectionAssert query();

    /**
     * 根据条件查询数据，并返回数据断言器
     *
     * @param where
     * @return
     */
    ICollectionAssert queryWhere(String where);

    /**
     * 根据条件查询数据，并返回数据断言器
     *
     * @param where
     * @return
     */
    ICollectionAssert queryWhere(IDataMap where);

    /**
     * 查询表中所有的数据，并且每条数据填充到PoJo中<br>
     * 返回 List<Obejct> 的断言器
     *
     * @param pojo
     * @return
     */
    ICollectionAssert queryList(Class pojo);

    /**
     * 查询单条数据，转换为PoJo对象。并且返回对象断言器
     *
     * @param pojo
     * @return
     */
    IObjectAssert queryAs(Class pojo);

    /**
     * 查询表count(*)的值，并且返回断言器
     *
     * @return
     */
    INumberAssert count();

    /**
     * 根据where删除数据
     *
     * @param where
     * @return
     */
    ITableOp deleteWhere(String where);

    /**
     * 打印数据库数据
     *
     * @param where
     * @return
     */
    ICollectionAssert printAndAssert(String where);
}
