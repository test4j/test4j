package cn.org.atool.fluent.mybatis.generator.demo.query;

import cn.org.atool.fluent.mybatis.base.IEntityQuery;
import cn.org.atool.fluent.mybatis.base.IProperty2Column;
import cn.org.atool.fluent.mybatis.util.MybatisUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import cn.org.atool.fluent.mybatis.generator.demo.entity.TUserEntity;
import cn.org.atool.fluent.mybatis.generator.demo.mapping.TUserMP;
import cn.org.atool.fluent.mybatis.generator.demo.mapping.TUserMP.Column;
import cn.org.atool.fluent.mybatis.generator.demo.query.TUserEntityWrapperHelper.And;
import cn.org.atool.fluent.mybatis.generator.demo.query.TUserEntityWrapperHelper.QueryOrder;

/**
 * @ClassName TUserEntityQuery
 * @Description TUserEntity查询（删除）条件
 *
 * @author ${author}
 */
public class TUserEntityQuery extends AbstractWrapper<TUserEntity, String, TUserEntityQuery>
    implements IEntityQuery<TUserEntityQuery, TUserEntity>, IProperty2Column {
    /**
     * 查询字段
     */
    private SharedString sqlSelect = new SharedString();

    public final And<TUserEntityQuery> and = new And<>(this);

    public final QueryOrder orderBy = new QueryOrder(this);

    public TUserEntityQuery(){
        this(null);
    }

    public TUserEntityQuery(TUserEntity entity){
        super.setEntity(entity);
        super.initNeed();
    }

    public TUserEntityQuery(TUserEntity entity, String... columns){
        super.setEntity(entity);
        super.initNeed();
        this.select(columns);
    }

    /**
     * 非对外公开的构造方法,只用于生产嵌套 sql
     */
    private TUserEntityQuery(TUserEntity entity, AtomicInteger paramNameSeq,
        Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments) {
        super.setEntity(entity);
        this.entityClass = TUserEntity.class;
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
    }

    @Override
    public TUserEntityQuery select(String... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            this.sqlSelect.setStringValue(String.join(StringPool.COMMA, columns));
        }
        return this;
    }

    @Override
    public TUserEntityQuery select(Predicate<TableFieldInfo> predicate) {
        this.entityClass = TUserEntity.class;
        this.sqlSelect.setStringValue(TableInfoHelper.getTableInfo(getCheckEntityClass()).chooseSelect(predicate));
        return this;
    }

    @Override
    public TUserEntityQuery select(Class<TUserEntity> entityClass, Predicate<TableFieldInfo> predicate) {
        this.entityClass = entityClass;
        this.sqlSelect.setStringValue(TableInfoHelper.getTableInfo(getCheckEntityClass()).chooseSelect(predicate));
        return this;
    }

    @Override
    public String getSqlSelect() {
        return sqlSelect.getStringValue();
    }

    /**
     * 只查询主键字段
     *
     * @return
     */
    public TUserEntityQuery selectId(){
        throw new RuntimeException("table primary undefined!");
    }

    @Override
    public TUserEntityQuery distinct(String... columns){
        if(ArrayUtils.isNotEmpty(columns)){
            this.sqlSelect.setStringValue(MybatisUtil.distinct(columns));
        }
        return this;
    }

    public TUserEntityQuery distinct(Predicate<TableFieldInfo> predicate) {
        this.entityClass = TUserEntity.class;
        this.sqlSelect.setStringValue(MybatisUtil.distinct(getCheckEntityClass(), predicate));
        return this;
    }

    public TUserEntityQuery distinct(Class<TUserEntity> entityClass, Predicate<TableFieldInfo> predicate) {
        this.entityClass = entityClass;
        this.sqlSelect.setStringValue(MybatisUtil.distinct(getCheckEntityClass(), predicate));
        return this;
    }


    /**
     * 暂不支持
     */
    public LambdaQueryWrapper<TUserEntity> lambda() {
        throw new RuntimeException("no support!");
    }

    /**
    * <p>
    * 用于生成嵌套 sql
    * 故 sqlSelect 不向下传递
    * </p>
    */
    @Override
    protected TUserEntityQuery instance() {
        return new TUserEntityQuery(entity, paramNameSeq, paramNameValuePairs, new MergeSegments());
    }

    @Override
    public Map<String, String> getProperty2Column(){
        return TUserMP.Property2Column;
    }

    @Override
    public TUserEntityQuery limit(int from, int limit){
        super.last(String.format("limit %d, %d", from, limit));
        return this;
    }

    @Override
    public TUserEntityQuery limit(int limit){
        super.last(String.format("limit %d", limit));
        return this;
    }
}