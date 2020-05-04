package cn.org.atool.fluent.mybatis.generator.demo.dao.base;

import cn.org.atool.fluent.mybatis.base.BaseDaoImpl;
import cn.org.atool.fluent.mybatis.generator.demo.entity.AddressEntity;
import cn.org.atool.fluent.mybatis.generator.demo.mapper.AddressMapper;
import cn.org.atool.fluent.mybatis.generator.demo.mapping.AddressMP;
import ${cfg.packEntityQuery}.${cfg.fileEntityQuery};
import ${cfg.packEntityUpdate}.${cfg.fileEntityUpdate};
import org.springframework.beans.factory.annotation.Autowired;

/**
* AddressEntity数据库操作服务类
 *
 * @author ${author}
*/
public abstract class AddressBaseDao extends BaseDaoImpl<AddressEntity, ${cfg.fileEntityQuery}, ${cfg.fileEntityUpdate}>
        implements AddressMP {

    @Autowired
    protected AddressMapper mapper;

    @Override
    public AddressMapper mapper() {
        return mapper;
    }

    @Override
    public ${cfg.fileEntityQuery} query(){
        return new ${cfg.fileEntityQuery}();
    }

    @Override
    public ${cfg.fileEntityUpdate} update(){
        return new ${cfg.fileEntityUpdate}();
    }

    @Override
    public String findPkColumn() {
        return AddressMP.Column.id;
    }
}
