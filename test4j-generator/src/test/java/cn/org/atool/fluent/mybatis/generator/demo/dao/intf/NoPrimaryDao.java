package cn.org.atool.fluent.mybatis.generator.demo.dao.intf;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.atool.fluent.mybatis.generator.demo.entity.NoPrimaryEntity;
import org.springframework.stereotype.Repository;

/**
 * @ClassName NoPrimaryDao
 * @Description NoPrimaryEntity数据操作接口
 *
 * @author ${author}
 */
@Repository
public interface NoPrimaryDao extends IBaseDao<NoPrimaryEntity>  {
}