package cn.org.atool.fluent.mybatis.generator.demo.mapper;

import cn.org.atool.fluent.mybatis.generator.demo.entity.NoAutoIdEntity;
import cn.org.atool.fluent.mybatis.mapper.IEntityMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * no_auto_id Mapper接口
 * </p>
 *
 * @author generate code
 */
@Mapper
@Component("newNoAutoIdMapper")
public interface NoAutoIdMapper extends IEntityMapper<NoAutoIdEntity>{
}