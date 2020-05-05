package cn.org.atool.fluent.mybatis.generator.demo.dao.intf;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.atool.fluent.mybatis.generator.demo.entity.NoAutoIdEntity;
import org.springframework.stereotype.Repository;

/**
 * @ClassName NoAutoIdDao
 * @Description NoAutoIdEntity数据操作接口
 *
 * @author ${author}
 */
@Repository
public interface NoAutoIdDao extends IBaseDao<NoAutoIdEntity>  {
}