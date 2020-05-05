package cn.org.atool.fluent.mybatis.generator.demo.dao.base;

import cn.org.atool.fluent.mybatis.base.BaseDaoImpl;
import cn.org.atool.fluent.mybatis.generator.demo.entity.UserEntity;
import cn.org.atool.fluent.mybatis.generator.demo.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generator.demo.mapping.UserMP;
import cn.org.atool.fluent.mybatis.generator.demo.query.UserEntityQuery;
import cn.org.atool.fluent.mybatis.generator.demo.query.UserEntityUpdate;
import org.springframework.beans.factory.annotation.Autowired;

/**
* UserEntity数据库操作服务类
 *
 * @author generate code
*/
public abstract class UserBaseDao extends BaseDaoImpl<UserEntity, UserEntityQuery, UserEntityUpdate>
        implements UserMP {

    @Autowired
    protected UserMapper mapper;

    @Override
    public UserMapper mapper() {
        return mapper;
    }

    @Override
    public UserEntityQuery query(){
        return new UserEntityQuery();
    }

    @Override
    public UserEntityUpdate update(){
        return new UserEntityUpdate();
    }

    @Override
    public String findPkColumn() {
        return UserMP.Column.id;
    }
}
