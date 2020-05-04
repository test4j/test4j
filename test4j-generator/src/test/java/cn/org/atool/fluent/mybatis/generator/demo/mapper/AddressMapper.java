package cn.org.atool.fluent.mybatis.generator.demo.mapper;

import cn.org.atool.fluent.mybatis.generator.demo.entity.AddressEntity;
import cn.org.atool.fluent.mybatis.mapper.IEntityMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper接口
 * </p>
 *
 * @author ${author}
 */
@Mapper
public interface AddressMapper extends IEntityMapper<AddressEntity>{
}