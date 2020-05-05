package cn.org.atool.fluent.mybatis.generator.demo.dao.intf;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.atool.fluent.mybatis.generator.demo.entity.TUserEntity;
import org.springframework.stereotype.Repository;

/**
 * @ClassName TUserDao
 * @Description TUserEntity数据操作接口
 *
 * @author ${author}
 */
@Repository
public interface TUserDao extends IBaseDao<TUserEntity>  {
}