package ${cfg.packDao};

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import ${package.Entity}.{importTypes=import java.io.Serializable;
import java.util.Date;, package=cn.org.atool.fluent.mybatis.generator.demo.entity, name=AddressEntity};
import org.springframework.stereotype.Repository;

/**
 * @ClassName ${cfg.fileDao}
 * @Description {importTypes=import java.io.Serializable;
import java.util.Date;, package=cn.org.atool.fluent.mybatis.generator.demo.entity, name=AddressEntity}数据操作接口
 *
 * @author ${author}
 */
@Repository
public interface ${cfg.fileDao} extends IBaseDao<{importTypes=import java.io.Serializable;
import java.util.Date;, package=cn.org.atool.fluent.mybatis.generator.demo.entity, name=AddressEntity}>  {
}