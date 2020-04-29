package cn.org.atool.fluent.mybatis.generator.demo.dao.intf;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.atool.fluent.mybatis.generator.demo.entity.AddressEntity;
import org.springframework.stereotype.Repository;

/**
 * @ClassName AddressDao
 * @Description AddressEntity数据操作接口
 *
 * @author generate code
 */
@Repository
public interface AddressDao extends IBaseDao<AddressEntity>  {
}