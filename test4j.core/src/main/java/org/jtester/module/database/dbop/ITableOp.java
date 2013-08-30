package org.jtester.module.database.dbop;

import org.jtester.hamcrest.iassert.object.intf.ICollectionAssert;
import org.jtester.hamcrest.iassert.object.intf.INumberAssert;
import org.jtester.hamcrest.iassert.object.intf.IObjectAssert;
import org.jtester.module.ICore.DataMap;
import org.jtester.tools.datagen.DataSet;

@SuppressWarnings("rawtypes")
public interface ITableOp {
    /**
     * 清空数据表
     * 
     * @return
     */
    ITableOp clean();

    /**
     * 根据json插入数据
     * 
     * @param json
     * @param more
     * @return
     */
    ITableOp insert(String json, String... more);

    /**
     * 批量插入count条数据
     * 
     * @param count 需要插入的数据的数量
     * @param data 需要插入的数据 Map，key:字段，value：n条数据的数组集合和策略
     * @return
     */
    ITableOp insert(int count, DataMap data);

    /**
     * 根据Map的key（表字段）插入数据
     * 
     * @param data
     * @param datas
     * @return
     */
    ITableOp insert(DataMap data, DataMap... datas);

    /**
     * 插入数据集
     * 
     * @param dataset
     * @return
     */
    ITableOp insert(DataSet dataset);

    /**
     * 提交数据
     */
    void commit();

    /**
     * 回滚数据
     */
    void rollback();

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
    ICollectionAssert queryWhere(DataMap where);

    /**
     * 查询表中所有的数据，并且每条数据填充到PoJo中<br>
     * 返回 List<Obejct> 的断言器
     * 
     * @param table
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
}
