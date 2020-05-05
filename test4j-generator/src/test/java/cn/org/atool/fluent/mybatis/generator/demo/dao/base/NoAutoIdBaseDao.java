package cn.org.atool.fluent.mybatis.generator.demo.dao.base;

import cn.org.atool.fluent.mybatis.base.BaseDaoImpl;
import cn.org.atool.fluent.mybatis.generator.demo.entity.NoAutoIdEntity;
import cn.org.atool.fluent.mybatis.generator.demo.mapper.NoAutoIdMapper;
import cn.org.atool.fluent.mybatis.generator.demo.mapping.NoAutoIdMP;
import cn.org.atool.fluent.mybatis.generator.demo.query.NoAutoIdEntityQuery;
import cn.org.atool.fluent.mybatis.generator.demo.query.NoAutoIdEntityUpdate;
import org.springframework.beans.factory.annotation.Autowired;

/**
* NoAutoIdEntity数据库操作服务类
 *
 * @author generate code
*/
public abstract class NoAutoIdBaseDao extends BaseDaoImpl<NoAutoIdEntity, NoAutoIdEntityQuery, NoAutoIdEntityUpdate>
        implements NoAutoIdMP {

    @Autowired
    protected NoAutoIdMapper mapper;

    @Override
    public NoAutoIdMapper mapper() {
        return mapper;
    }

    @Override
    public NoAutoIdEntityQuery query(){
        return new NoAutoIdEntityQuery();
    }

    @Override
    public NoAutoIdEntityUpdate update(){
        return new NoAutoIdEntityUpdate();
    }

    @Override
    public String findPkColumn() {
        return NoAutoIdMP.Column.id;
    }
}
