package cn.org.atool.fluent.mybatis.generator.demo.dao.base;

import cn.org.atool.fluent.mybatis.base.BaseDaoImpl;
import cn.org.atool.fluent.mybatis.generator.demo.entity.AddressEntity;
import cn.org.atool.fluent.mybatis.generator.demo.mapper.AddressMapper;
import cn.org.atool.fluent.mybatis.generator.demo.mapping.AddressMP;
import cn.org.atool.fluent.mybatis.generator.demo.query.AddressEntityQuery;
import cn.org.atool.fluent.mybatis.generator.demo.query.AddressEntityUpdate;
import org.springframework.beans.factory.annotation.Autowired;

/**
* AddressEntity数据库操作服务类
 *
 * @author generate code
*/
public abstract class AddressBaseDao extends BaseDaoImpl<AddressEntity, AddressEntityQuery, AddressEntityUpdate>
        implements AddressMP {

    @Autowired
    protected AddressMapper mapper;

    @Override
    public AddressMapper mapper() {
        return mapper;
    }

    @Override
    public AddressEntityQuery query(){
        return new AddressEntityQuery();
    }

    @Override
    public AddressEntityUpdate update(){
        return new AddressEntityUpdate();
    }

    @Override
    public String findPkColumn() {
        return AddressMP.Column.id;
    }
}
