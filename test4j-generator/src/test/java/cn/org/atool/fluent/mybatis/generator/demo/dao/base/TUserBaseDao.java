package cn.org.atool.fluent.mybatis.generator.demo.dao.base;

import cn.org.atool.fluent.mybatis.base.BaseDaoImpl;
import cn.org.atool.fluent.mybatis.generator.demo.entity.TUserEntity;
import cn.org.atool.fluent.mybatis.generator.demo.mapper.TUserMapper;
import cn.org.atool.fluent.mybatis.generator.demo.mapping.TUserMP;
import cn.org.atool.fluent.mybatis.generator.demo.query.TUserEntityQuery;
import cn.org.atool.fluent.mybatis.generator.demo.query.TUserEntityUpdate;
import org.springframework.beans.factory.annotation.Autowired;

/**
* TUserEntity数据库操作服务类
 *
 * @author ${author}
*/
public abstract class TUserBaseDao extends BaseDaoImpl<TUserEntity, TUserEntityQuery, TUserEntityUpdate>
        implements TUserMP {

    @Autowired
    protected TUserMapper mapper;

    @Override
    public TUserMapper mapper() {
        return mapper;
    }

    @Override
    public TUserEntityQuery query(){
        return new TUserEntityQuery();
    }

    @Override
    public TUserEntityUpdate update(){
        return new TUserEntityUpdate();
    }

    @Override
    public String findPkColumn() {
        return TUserMP.Column.id;
    }
}
